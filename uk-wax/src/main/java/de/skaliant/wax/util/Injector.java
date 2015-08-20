package de.skaliant.wax.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import de.skaliant.wax.app.Param;
import de.skaliant.wax.core.model.ParameterProvider;
import de.skaliant.wax.util.logging.Log;


/**
 * Class responsible for injecting parameter and path values into bean properties and method arguments.
 *
 * @author Udo Kastilan
 */
public class Injector
{
	private final static Log LOG = Log.get(Injector.class);

	
	/**
	 * Inject named parameters and special objects into bean properties.
	 * 
	 * @param instance The bean
	 * @param byName Named parameters
	 */
	public static void injectBeanProperties(Object instance, ParameterProvider byName)
	{
		injectBeanProperties(instance, byName, null);
	}

	
	/**
	 * Inject named parameters and special objects into bean properties.
	 * 
	 * @param instance The bean
	 * @param byName Named parameters
	 * @param byType Special objects to be injected by their class type
	 */
	public static void injectBeanProperties(Object instance, ParameterProvider byName, List<?> byType)
	{
		Bean<?> bean = Bean.wrap(instance);
		
		try
		{
			/*
			 * Try to assign objects by type, if any
			 */
			if ((byType != null) && !byType.isEmpty())
			{
				for (PropertyDescriptor pd : bean.getProperties(Bean.Accessibility.WRITEABLE))
				{
					for (Object special : byType)
					{
						if (pd.getPropertyType().isAssignableFrom(special.getClass()))
						{
							pd.getWriteMethod().invoke(instance, special);
							break;
						}
					}
				}
			}
			/*
			 * Resolve parameter names, split at dots; also accept indexed notation for arrays and lists
			 */
			for (String n : byName.getParameterNames())
			{
				/*
				 * In case the name contains at least one dot: resolve bean paths and apply values to the
				 * last target in path
				 */
				if (n.indexOf('.') != -1)
				{
					List<String> nameChain = MiscUtils.split(n, '.');
					Bean<?> target = bean;
					
					/*
					 * 1. Resolve the path to the final target property
					 */
					while ((target != null) && (nameChain.size() > 1))
					{
						target = new PropertyInjection(target, nameChain.remove(0)).resolve();
					}
					/*
					 * 2. Apply to the final destination
					 */
					if ((target != null) && (nameChain.size() == 1))
					{
						new PropertyInjection(target, nameChain.get(0)).apply(byName.getParameterValues(n));
					}
				}
				else
				{
					/*
					 * Otherwise: simply apply the value(s) to the property itself
					 */
					new PropertyInjection(bean, n).apply(byName.getParameterValues(n));
				}
			}
		}
		catch (Exception ex)
		{
			LOG.warn("Trouble in the property section", ex);
		}
	}

	
	/**
	 * Inject method arguments and return an array of objects considered suitable for an invocation.
	 * 
	 * @param method Method
	 * @param byName Parameters as name/value pairs
	 * @param pathInfoParts Path info split at "/" into parts
	 * @param byType Parameters to be injected by their class type
	 * @return Method argument array
	 */
	public static Object[] injectMethodArguments(Method method, ParameterProvider byName, List<String> pathInfoParts, List<?> byType)
	{
		Type[] paramTypes = method.getGenericParameterTypes();
		Object[] args = new Object[paramTypes.length];
		/*
		 * If there are any method parameters, try to apply request parameters and path info strings to them.
		 */
		if (paramTypes.length != 0)
		{
			Annotation[][] paramAnnos = method.getParameterAnnotations();
			int pix = 0;
			/*
			 * Start by applying default values in case there are primitive values. We don't want to assign
			 * null values for these.
			 */
			for (int i = 0; i < paramTypes.length; i++)
			{
				args[i] = TypeUtils.defaultValue(paramTypes[i]);
			}
			/*
			 * Iterate over parameter descriptions; unfortunately, there is no reliable way of getting parameter
			 * names via reflection (they're included with debug info only). So I check for a Param annotation
			 * first; if there is none, I try to assign path info values according to order (pix = path info
			 * index)
			 */
			for (int i = 0; i < paramTypes.length; i++)
			{
				boolean done = false;
				
				for (Annotation anno : paramAnnos[i])
				{
					if (anno instanceof Param)
					{
						Param paranno = (Param) anno;
						
						if (byName.isParameterPresent(paranno.value()));
						{
							args[i] = Converter.convertValue(paramTypes[i], byName.getParameterValues(paranno.value()));
						}
						done = true;
						break;
					}
				}
				/*
				 * If not done through naming by annotations ...
				 */
				if (!done)
				{
					/*
					 * ... try to assign special objects by type
					 */
					for (Object special : byType)
					{
						if ((paramTypes[i] instanceof Class) && ((Class<?>) paramTypes[i]).isAssignableFrom(special.getClass()))
						{
							args[i] = special;
							done = true;
							break;
						}
					}
					/*
					 * ... or anything else via path info
					 */
					if ((!done) && (pix < pathInfoParts.size()))
					{
						args[i] = Converter.convertValue(paramTypes[i], pathInfoParts.get(pix++));
					}
				}
			}
		}
		return args;
	}
	
	
	private static class PropertyInjection
	{
		private Bean<?> bean = null;
		private String propertyName = null;
		private Integer index = null;
		
		
		private PropertyInjection(Bean<?> bean, String propertyName)
		{
			int rectOpen = propertyName.indexOf('[');
			int rectClose = propertyName.indexOf(']');
			
			this.bean = bean;
			/*
			 * Support for indexed properties of type array or List
			 */
			if ((rectOpen != -1) && (rectClose != -1))
			{
				/*
				 * Sanity checks: opening bracket must not be first char, closing must be last, and there must be
				 * at least one char between the two
				 */
				if ((rectOpen > 0) && (rectClose > (rectOpen + 1)) && (rectClose == (propertyName.length() - 1)))
				{
					try
					{
						index = Integer.valueOf(propertyName.substring(rectOpen + 1, rectClose));
					}
					catch (Exception ex)
					{
						//
					}
				}
				/*
				 * Cut off everything starting with the first bracket, be it opening or closing
				 */
				propertyName = propertyName.substring(0, Math.min(rectOpen, rectClose));
			}
			this.propertyName = propertyName;
		}
		
		
		/**
		 * Assumes the current setting is a bean property statement that should be resolved.
		 * 
		 * @return Result of the statement, which might be null for different reasons
		 */
		private Bean<?> resolve()
		{
			Object r = bean.resolve(propertyName);
			
			if ((r != null) && (index != null))
			{
				if (r.getClass().isArray() && (Array.getLength(r) > index))
				{
					r = Array.get(r, index);
				}
				else if ((r instanceof List) && (((List<?>) r).size() > index))
				{
					r = ((List<?>) r).get(index);
				}
				else
				{
					r = null;
				}
			}
			return r == null ? null : Bean.wrap(r);
		}
		
		
		/**
		 * Assumes the current setting is a property statement the value(s) should be assigned to.
		 * 
		 * @param values Value(s)
		 * @throws Exception
		 */
		private void apply(String[] values)
		{
			if (bean.hasProperty(propertyName) && (values != null) && (values.length != 0))
			{
				Type type = bean.getPropertyType(propertyName);
				
				if (index != null)
				{
					if (bean.isReadable(propertyName))
					{
						Object current = bean.get(propertyName);
						
						if (current instanceof List)
						{
							@SuppressWarnings("unchecked")
							List<Object> ls = (List<Object>) current;
							Type elementType = Object.class;
							Object v = null;

							for (Type t : TypeUtils.typeArguments(type))
							{
								elementType = t;
								break;
							}
							v = Converter.convertValue(elementType, values);
							if (v != null)
							{
								while (ls.size() < (index + 1))
								{
									ls.add(null);
								}
								ls.set(index, v);
							}
						}
						else if ((current != null) && current.getClass().isArray() && (Array.getLength(current) > index))
						{
							Object v = Converter.convertValue(current.getClass().getComponentType(), values);
							
							if (v != null)
							{
								Array.set(current, index, v);
							}
						}
					}
				}
				else
				{
					Object v = Converter.convertValue(type, values);
					
					if ((v != null) && (bean.isWriteable(propertyName)))
					{
						bean.set(propertyName, v);
					}
				}
			}
		}
	}
}
