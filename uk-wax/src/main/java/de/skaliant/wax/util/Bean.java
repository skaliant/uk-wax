package de.skaliant.wax.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class Bean<B>
{
	private Map<String, PropertyDescriptor> props = Collections.emptyMap();
	private Class<B> type = null;
	private B bean = null;
	
	
	@SuppressWarnings("unchecked")
	private Bean(B bean)
	{
		this((Class<B>) bean.getClass());
		this.bean = bean;
	}

	
	private Bean(Class<B> type)
	{
		this.type = type;
		props = map(getProperties(type));
	}

	
	public Type getPropertyType(String name)
	{
		PropertyDescriptor pd = props.get(name);
		Type t = null;
		
		if (pd != null)
		{
			if (pd.getReadMethod() != null)
			{
				t = pd.getReadMethod().getGenericReturnType();
			}
			else if (pd.getWriteMethod() != null)
			{
				t = pd.getWriteMethod().getGenericParameterTypes()[0];
			}
		}
		return t;
	}
	
	
	public boolean hasProperty(String name)
	{
		return props.containsKey(name);
	}
	
	
	public boolean isReadable(String name)
	{
		PropertyDescriptor pd = props.get(name);
		
		return (pd != null) && (pd.getReadMethod() != null);
	}
	
	
	public boolean isWriteable(String name)
	{
		PropertyDescriptor pd = props.get(name);
		
		return (pd != null) && (pd.getWriteMethod() != null);
	}
	
	
	public PropertyDescriptor getProperty(String name)
	{
		return props.get(name);
	}
	
	
	public Collection<PropertyDescriptor> getProperties(Accessibility access)
	{
		List<PropertyDescriptor> ls = new ArrayList<PropertyDescriptor>();
		
		try
		{
			for (PropertyDescriptor pd : props.values())
			{
				switch (access)
				{
					case ANY:
						ls.add(pd);
						break;
					case READ_ONLY:
						if ((pd.getReadMethod() != null) && (pd.getWriteMethod() == null))
						{
							ls.add(pd);
						}
						break;
					case READ_WRITE:
						if ((pd.getReadMethod() != null) && (pd.getWriteMethod() != null))
						{
							ls.add(pd);
						}
						break;
					case READABLE:
						if (pd.getReadMethod() != null)
						{
							ls.add(pd);
						}
						break;
					case WRITE_ONLY:
						if ((pd.getReadMethod() == null) && (pd.getWriteMethod() != null))
						{
							ls.add(pd);
						}
						break;
					case WRITEABLE:
						if (pd.getWriteMethod() != null)
						{
							ls.add(pd);
						}
						break;
				}
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		return ls;
	}

	
	public <T> T get(String name, Class<T> type)
	{
		PropertyDescriptor pd = props.get(name);
		
		if (pd == null)
		{
			throw new IllegalArgumentException("No property \"" + name + "\" in class " + this.type.getName());
		}
		if (pd.getReadMethod() == null)
		{
			throw new IllegalArgumentException("Property \"" + name + "\" in class " + this.type.getName() + " is not readable");
		}
		try
		{
			return type.cast(pd.getReadMethod().invoke(bean));
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	
	public Object get(String name)
	{
		PropertyDescriptor pd = props.get(name);
		
		if (pd == null)
		{
			throw new IllegalArgumentException("No property \"" + name + "\" in class " + type.getName());
		}
		if (pd.getReadMethod() == null)
		{
			throw new IllegalArgumentException("Property \"" + name + "\" in class " + type.getName() + " is not readable");
		}
		try
		{
			return pd.getReadMethod().invoke(bean);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	
	public void set(String name, Object value)
	{
		PropertyDescriptor pd = props.get(name);
		
		if (pd == null)
		{
			throw new IllegalArgumentException("No property \"" + name + "\" in class " + type.getName());
		}
		if (pd.getWriteMethod() == null)
		{
			throw new IllegalArgumentException("Property \"" + name + "\" in class " + type.getName() + " is not writeable");
		}
		try
		{
			pd.getWriteMethod().invoke(bean, value);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	
	public B getInstance()
	{
		return bean;
	}

	
	public void setInstance(B bean)
	{
		this.bean = bean;
	}
	
	
	public B newInstance()
	{
		try
		{
			return type.newInstance();
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	
	public static <B> Bean<B> type(Class<B> type)
	{
		return new Bean<B>(type);
	}
	
	
	public static <B> Bean<B> wrap(B bean)
	{
		return new Bean<B>(bean);
	}
	
	
	public Bean<?> crawl(String statement)
	{
		return crawl(MiscUtils.split(statement, '.'));
	}
	
	
	public Bean<?> crawl(List<String> statements)
	{
		Object resolved = resolve(this, statements);
		
		if (resolved != null)
		{
			return wrap(resolved);
		}
		return null;
	}
	
	
	public static Map<String, PropertyDescriptor> map(Collection<PropertyDescriptor> props)
	{ 
		Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
		
		for (PropertyDescriptor pd : props)
		{
			map.put(pd.getName(), pd);
		}
		return map;
	}
	
	
	public Object resolve(String statement)
	{
		if (statement.indexOf('.') == -1)
		{
			return resolve(bean, Collections.singletonList(statement));
		}
		return resolve(bean, MiscUtils.split(statement, '.'));
	}
	
	
	public Object resolve(List<String> statements)
	{
		return resolve(bean, statements);
	}
	
	
	public static Object resolve(Object bean, String statement)
	{
		return resolve(bean, MiscUtils.split(statement, '.'));
	}
	
	
	public static Object resolve(Object bean, List<String> statements)
	{ 
		Object o = bean;
		
		for (int i = 0, len = statements.size(); (i < len) && (o != null); i++)
		{
			String s = statements.get(i); 
			
			if (o instanceof Map)
			{
				o = ((Map<?, ?>) o).get(s);
			}
			else
			{
				boolean found = false;
				
				for (PropertyDescriptor pd : getProperties(o.getClass(), Accessibility.READABLE))
				{
					if (pd.getName().equals(s))
					{
						try
						{
							o = pd.getReadMethod().invoke(o);
						}
						catch (Exception ex)
						{
							throw new RuntimeException(ex);
						}
						found = true;
						break;
					}
				}
				if (!found)
				{
					o = null;
				}
			}
		}
		return o;
	}
	
	
	public static List<PropertyDescriptor> getProperties(Class<?> type)
	{
		return getProperties(type, Accessibility.ANY);
	}
	
	
	public static List<PropertyDescriptor> getProperties(Class<?> type, Accessibility access)
	{
		List<PropertyDescriptor> ls = new ArrayList<PropertyDescriptor>();
		
		try
		{
			for (PropertyDescriptor pd : Introspector.getBeanInfo(type).getPropertyDescriptors())
			{
				switch (access)
				{
					case ANY:
						ls.add(pd);
						break;
					case READ_ONLY:
						if ((pd.getReadMethod() != null) && (pd.getWriteMethod() == null))
						{
							ls.add(pd);
						}
						break;
					case READ_WRITE:
						if ((pd.getReadMethod() != null) && (pd.getWriteMethod() != null))
						{
							ls.add(pd);
						}
						break;
					case READABLE:
						if (pd.getReadMethod() != null)
						{
							ls.add(pd);
						}
						break;
					case WRITE_ONLY:
						if ((pd.getReadMethod() == null) && (pd.getWriteMethod() != null))
						{
							ls.add(pd);
						}
						break;
					case WRITEABLE:
						if (pd.getWriteMethod() != null)
						{
							ls.add(pd);
						}
						break;
				}
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		return ls;
	}


	public static enum Accessibility
	{
		READ_ONLY, WRITE_ONLY, READABLE, WRITEABLE, READ_WRITE, ANY;
	}
}
