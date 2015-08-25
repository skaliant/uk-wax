package de.skaliant.wax.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
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
		ParameterValueProvider pvp = new ParameterValueProvider();
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
				pvp.arr = byName.getParameterValues(n);
				try
				{
					StatementResolver.setValueTo(bean.getInstance(), n, pvp);
				}
				catch (Exception ex)
				{
					//
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
	
	
	private static class ParameterValueProvider
		implements StatementResolver.ValueProvider
	{
		private String[] arr = null;
		

		public Object provide(Type type)
		{
			return Converter.convertValue(type, arr);
		}
	}
}
