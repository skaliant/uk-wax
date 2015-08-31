package de.skaliant.wax.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

import de.skaliant.wax.util.logging.Log;


/**
 * Utility class for resolving potentially complex bean property statements. Typically used for injecting
 * parameter values to controller bean properties, that's why there's a convenience method to directly set
 * a value to a bean using the full statement. The easiest case of a statement would be a simple property 
 * name such as "name". Properties may also be given numerically indexed (0-based), such as "phone[1]", so 
 * the resolving process will check for a "phone" property having an indexed data type, i.e. an array or a 
 * List. For indexed access, the semantics are slightly different: while normal property access requires 
 * only the setter method to be present, indexed access will need the getter method, call it, and inject 
 * the value into the resulting array or List. Last but not least, property statements (indexed or not) may 
 * be combined into chains separated by dot notation, e.g. "user.phone[1].areaCode". The resolver will try
 * to follow that chain as far as possible (stopped by missing properties or null values) and inject the value
 * into the instance presented by the last part of this chain. So apart from the null checks, the statement 
 * "<code>user.phone[1].areaCode</code>" for bean <code>b</code> and value <code>v</code> roughly translates 
 * to <code>b.getUser().getPhone()[1].setAreaCode(v)</code> or 
 * <code>b.getUser().getPhone().get(1).setAreaCode(v)</code>. 
 *
 * @author Udo Kastilan
 */
public class StatementResolver
{
	private final Log LOG = Log.get(StatementResolver.class);
	
	
	/**
	 * Convenience method: take a bean, a complex statement consisting of one or more property names separated by dots,
	 * and an object which is able to deliver a value matching a certain data type, and set the property resolved to 
	 * by using the statement to this value. Property names may be indexed (array-like syntax), if the data type
	 * is either List or an array. Examples for statements are "name", "owner.name", "owner.children[2].name".
	 * 
	 * @param bean The original bean
	 * @param complexStatement Statement (chain of possibly indexed property names) to be applied to the bean
	 * @param vap Provides the value to be assigned
	 * @throws Exception In case the value may not be set (e.g. wrong type)
	 */
	public void setValueTo(Object bean, String complexStatement, ValueProvider vap)
		throws Exception
	{
		if ((bean == null) || (complexStatement == null) || (vap == null))
		{
			return;
		}
		List<String> ls = MiscUtils.split(complexStatement, '.');
		StatementTarget st = null;
		/*
		 * Empty statements resolve to nothing
		 */
		if (ls.isEmpty())
		{
			return;
		}
		/*
		 * Consume all statement parts but the last one, try to follow the path
		 * unless we find null
		 */
		while ((bean != null) && (ls.size() > 1))
		{
			st = find(bean, ls.remove(0));
			if (st != null)
			{
				Object o = st.read();
				
				bean = (o == null) ? null : o;
			}
		}
		/*
		 * Still a bean at hand? Inject value into the property referred to by the remaining 
		 * statement part
		 */
		if (bean != null)
		{
			st = find(bean, ls.get(0));
			if (st != null)
			{
				Object v = vap.provide(st.getType());
				
				if (v != null)
				{
					st.write(v);
				}
			}
		}
	}
	
	
	/**
	 * Resolve a single statement part (possibly indexed) on the given bean, and return an object which
	 * provides read or write access to the found target. In case the statement is indexed, the value will
	 * be injected into the indexed value of the property, not the property field itself. A result object 
	 * will only be delivered, if the statement is valid; the rules are as follows:
	 * <ul>
	 *   <li>Statement is either a simple name (<code>name</code>) or an indexed name (<code>name[index]</code>)</li>
	 *   <li>The property exists</li>
	 *   <li>If an index is present,</li>
	 *   <ul>
	 *     <li>it is numeric and not negative</li>
	 *     <li>the property's data type is indexed, i.e. either a List or an array of some sort</li>
	 *     <li>the property's value is not null</li>
	 *     <li>if the property's value is an array, it is large enough for the index</li>
	 *   </ul>
	 * </ul>
	 * 
	 * @param bean Bean
	 * @param atomicStatement Simple statement (e.g. a simple property name), possibly indexed
	 * @return Instance for accessing the target, or null if not found or not appropriate
	 */
	public StatementTarget find(Object bean, String atomicStatement)
	{
		if (bean == null)
		{
			return null;
		}
		StatementTarget target = null;
		Integer index = null;
		Bean<?> b = Bean.wrap(bean);
		int rectOpen = atomicStatement.indexOf('[');
		int rectClose = atomicStatement.indexOf(']');
		/*
		 * Sanity checks: 
		 * - either none or both brackets present
		 * - opening one must come before the closing one
		 * - opening one must not be the first char
		 * - there must be at least one char between the brackets
		 * - closing one must be last char
		 */
		if ((rectOpen == -1) ^ (rectClose == -1))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Incomplete index brackets: \"" + atomicStatement + '"');
			}
			return null;
		}
		if ((rectOpen != -1) && (rectClose != -1))
		{
			if ((rectOpen == 0) || (rectClose < (rectOpen + 2)) || (rectClose != (atomicStatement.length() - 1)))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Invalid index syntax: \"" + atomicStatement + '"');
				}
				return null;
			}
			try
			{
				index = Integer.valueOf(atomicStatement.substring(rectOpen + 1, rectClose));
			}
			catch (Exception ex)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Index value not numeric: \"" + atomicStatement + '"');
				}
				return null;
			}
			if (index < 0)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Index value must not be negative: \"" + atomicStatement + '"');
				}
				return null;
			}
			atomicStatement = atomicStatement.substring(0, rectOpen);
		}
		/*
		 * If there's no property with that name, bail out
		 */
		if (!b.hasProperty(atomicStatement))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No property named \"" + atomicStatement + "\" available in type " + b.getInstance().getClass().getName());
			}
			return null;
		}
		PropertyDescriptor pd = b.getProperty(atomicStatement);
		boolean isArray = pd.getPropertyType().isArray();
		boolean isList = List.class.isAssignableFrom(pd.getPropertyType());
		/*
		 * No index? Perfectly normal property access; anything else will need to pass
		 * some more sanity checks:
		 * - must be indexed data type
		 * - property must be readable
		 */
		if (index == null)
		{
			target = new PropertyTarget(b, atomicStatement);
		}
		else if (!(isArray || isList))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Property \"" + atomicStatement + "\" not an indexed data type in " + b.getInstance().getClass().getName());
			}
		}
		else if (pd.getReadMethod() == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Property \"" + atomicStatement + "\" not readable in " + b.getInstance().getClass().getName());
			}
		}
		else
		{
			Object o = null;
			
			try
			{
				o = pd.getReadMethod().invoke(b.getInstance());
			}
			catch (Exception ex)
			{
				throw new RuntimeException(ex);
			}
			if (o == null)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Indexed property value of \"" + atomicStatement + "\" must not be null");
				}
			}
			else if (isList)
			{
				Type listType = TypeUtils.firstGenericArgument(b.getPropertyType(atomicStatement));
				
				if (listType != null)
				{
					target = new ListIndexTarget(castToObjectList(o), listType, index);
				}
			}
			else if (isArray)
			{
				int len = Array.getLength(o);
				
				if (len > index)
				{
					target = new ArrayIndexTarget(o, index);
				}
				else
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Array value of \"" + atomicStatement + "\" too small for index " + index);
					}
				}
			}
		}
		return target;
	}

	
	@SuppressWarnings("unchecked")
	private static List<Object> castToObjectList(Object list)
	{
		return (List<Object>) list;
	}
	

	/**
	 * Interface for an object that delivers a value matching the given data type. The type
	 * might be a complex, parameterized type such as "List&lt;String&gt;". In case the type
	 * is not supported, just return null, so nothing will be done.
	 */
	public static interface ValueProvider
	{
		/**
		 * Provide the value. A null value will be ignored, it will not be set.
		 * 
		 * @param type Data type
		 * @return Matching value
		 */
		Object provide(Type type);
	}
	
	
	/**
	 * Instance for the target of a statement.
	 */
	public static interface StatementTarget
	{
		/**
		 * Read the target's value, if possible. If the target is not readable,
		 * a null value will be returned. Use {@link #isReadable()} to distinguish.
		 * 
		 * @return
		 */
		Object read();
		
		/**
		 * Tries to set the target to the given value. Will ignore the call if the target
		 * is not writeable.
		 * 
		 * @param value Value
		 * @throws Exception In case the value cannot be set due to an internal error (such as wrong type)
		 */
		void write(Object value)
			throws Exception;

		/**
		 * Delivers the data type of the target. This is either the type of a property or, in case of indexed access, 
		 * the component type of an array or the type argument of a List respectively.
		 * 
		 * @return
		 */
		Type getType();

		/**
		 * Is the target readable? Will be true for properties having a getter method and for indexed access to arrays and Lists.
		 * 
		 * @return Readable?
		 */
		boolean isReadable();

		/**
		 * Is the target writeable? Will be true for properties having a setter method and for indexed access to arrays and Lists.
		 * 
		 * @return Writeable?
		 */
		boolean isWriteable();
	}
	
	
	/**
	 * Implementation for normal property access.
	 */
	private static class PropertyTarget
		implements StatementTarget
	{
		private Bean<?> bean = null;
		private String name = null;
		
		
		private PropertyTarget(Bean<?> bean, String name)
		{
			this.bean = bean;
			this.name = name;
		}
		
		
		public Type getType()
		{
			return bean.getPropertyType(name);
		}
		
		
		public boolean isReadable()
		{
			return bean.isReadable(name);
		}
		
		
		public boolean isWriteable()
		{
			return bean.isWriteable(name);
		}
		
		
		public Object read()
		{
			if (bean.isReadable(name))
			{
				return bean.get(name);
			}
			return null;
		}
		
		
		public void write(Object value)
			throws Exception
		{
			if (bean.isWriteable(name))
			{
				bean.set(name, value);
			}
		}
	}
	
	
	/**
	 * Implementation for indexed access on array types.
	 */
	private static class ArrayIndexTarget
		implements StatementTarget
	{
		private Object array = null;
		private int index = 0;
		
		
		private ArrayIndexTarget(Object array, int index)
		{
			this.array = array;
			this.index = index;
		}

		
		public Type getType()
		{
			return array.getClass().getComponentType();
		}
		
		
		public boolean isReadable()
		{
			return true;
		}
		
		
		public boolean isWriteable()
		{
			return true;
		}
		

		public Object read()
		{
			return Array.get(array, index);
		}


		public void write(Object value)
		{
			Array.set(array, index, value);
		}
	}
	
	
	/**
	 * Implementation for indexed access on Lists.
	 */
	private static class ListIndexTarget
		implements StatementTarget
	{
		private List<Object> list = null;
		private Type type = null;
		private int index = 0;
		
		
		private ListIndexTarget(List<Object> list, Type type, int index)
		{
			this.list = list;
			this.type = type;
			this.index = index;
		}

		
		public Type getType()
		{
			return type;
		}
		
		
		public boolean isReadable()
		{
			return true;
		}
		
		
		public boolean isWriteable()
		{
			return true;
		}
		

		public Object read()
		{
			return (index < list.size()) ? list.get(index) : null;
		}


		public void write(Object value)
		{
			while (list.size() < (index + 1))
			{
				list.add(null);
			}
			list.set(index, value);
		}
	}
}
