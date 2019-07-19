package de.skaliant.wax.util.beans;

import static java.util.stream.Collectors.toList;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class Bean<B> {
	private Map<String, PropertyDescriptor> props = Collections.emptyMap();
	private Class<B> type = null;
	private B bean = null;


	@SuppressWarnings("unchecked")
	private Bean(B bean) {
		this((Class<B>) bean.getClass());
		this.bean = bean;
	}


	private Bean(Class<B> type) {
		this.type = type;
		props = map(getProperties(type));
	}


	public Type getPropertyType(String name) {
		PropertyDescriptor pd = props.get(name);
		Type t = null;

		if (pd != null) {
			if (pd.getReadMethod() != null) {
				t = pd.getReadMethod().getGenericReturnType();
			} else if (pd.getWriteMethod() != null) {
				t = pd.getWriteMethod().getGenericParameterTypes()[0];
			}
		}
		return t;
	}


	public boolean hasProperty(String name) {
		return props.containsKey(name);
	}


	public boolean isReadable(String name) {
		PropertyDescriptor pd = props.get(name);

		return (pd != null) && (pd.getReadMethod() != null);
	}


	public boolean isWriteable(String name) {
		PropertyDescriptor pd = props.get(name);

		return (pd != null) && (pd.getWriteMethod() != null);
	}


	public PropertyDescriptor getProperty(String name) {
		return props.get(name);
	}


	public Collection<PropertyDescriptor> getProperties(Accessibility access) {
		return getProperties(type, access);
	}


	public <T> T get(String name, Class<T> type) {
		PropertyDescriptor pd = props.get(name);

		if (pd == null) {
			throw new IllegalArgumentException(
					"No property \"" + name + "\" in class " + this.type.getName());
		}
		if (pd.getReadMethod() == null) {
			throw new IllegalArgumentException("Property \"" + name + "\" in class "
					+ this.type.getName() + " is not readable");
		}
		try {
			return type.cast(pd.getReadMethod().invoke(bean));
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	public Object get(String name) {
		PropertyDescriptor pd = props.get(name);

		if (pd == null) {
			throw new IllegalArgumentException(
					"No property \"" + name + "\" in class " + type.getName());
		}
		if (pd.getReadMethod() == null) {
			throw new IllegalArgumentException("Property \"" + name + "\" in class "
					+ type.getName() + " is not readable");
		}
		try {
			return pd.getReadMethod().invoke(bean);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	public void set(String name, Object value) {
		PropertyDescriptor pd = props.get(name);

		if (pd == null) {
			throw new IllegalArgumentException(
					"No property \"" + name + "\" in class " + type.getName());
		}
		if (pd.getWriteMethod() == null) {
			throw new IllegalArgumentException("Property \"" + name + "\" in class "
					+ type.getName() + " is not writeable");
		}
		try {
			pd.getWriteMethod().invoke(bean, value);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	public Class<B> getBeanType() {
		return type;
	}


	public B getInstance() {
		return bean;
	}


	public void setInstance(B bean) {
		this.bean = bean;
	}


	public B newInstance() {
		try {
			return type.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	public static <B> Bean<B> type(Class<B> type) {
		return new Bean<B>(type);
	}


	public static <B> Bean<B> wrap(B bean) {
		return new Bean<B>(bean);
	}


	public Bean<?> crawl(String statement) {
		return crawl(MiscUtils.splitAtChar(statement, '.'));
	}


	public Bean<?> crawl(List<String> statements) {
		Object resolved = resolve(this, statements);

		if (resolved != null) {
			return wrap(resolved);
		}
		return null;
	}


	public static Map<String, PropertyDescriptor> map(
			Collection<PropertyDescriptor> props) {
		Map<String, PropertyDescriptor> map = new HashMap<>();

		for (PropertyDescriptor pd : props) {
			map.put(pd.getName(), pd);
		}
		return map;
	}


	public Object resolve(String statement) {
		if (statement.indexOf('.') == -1) {
			return resolve(bean, Collections.singletonList(statement));
		}
		return resolve(bean, MiscUtils.splitAtChar(statement, '.'));
	}


	public Object resolve(List<String> statements) {
		return resolve(bean, statements);
	}


	public static Object resolve(Object bean, String statement) {
		return resolve(bean, MiscUtils.splitAtChar(statement, '.'));
	}


	public static Object resolve(Object bean, List<String> statements) {
		Object o = bean;

		for (int i = 0, len = statements.size(); (i < len) && (o != null); i++) {
			String s = statements.get(i);

			if (o instanceof Map) {
				o = ((Map<?, ?>) o).get(s);
			} else {
				Bean<Object> b = new Bean<Object>(o);

				if (b.isReadable(s)) {
					o = new Bean<Object>(o).get(s);
				} else {
					o = null;
				}
			}
		}
		return o;
	}


	public static PropertyDescriptor findPropertyByName(Class<?> type,
			String name) {
		for (PropertyDescriptor pd : getProperties(type)) {
			if (pd.getName().equals(name)) {
				return pd;
			}
		}

		return null;
	}


	public static List<PropertyDescriptor> getProperties(Class<?> type) {
		return getProperties(type, Accessibility.ANY);
	}


	public static List<PropertyDescriptor> getProperties(Class<?> type,
			Accessibility access) {
		List<PropertyDescriptor> all;

		try {
			all = Arrays
					.asList(Introspector.getBeanInfo(type).getPropertyDescriptors());
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return all.stream().filter(getFilter(access)).collect(toList());
	}


	private static Predicate<? super PropertyDescriptor> getFilter(
			Accessibility access) {
		switch (access) {
			case ANY:
				return p -> true;
			case READ_ONLY:
				return p -> (p.getReadMethod() != null) && (p.getWriteMethod() == null);
			case READ_WRITE:
				return p -> (p.getReadMethod() != null) && (p.getWriteMethod() != null);
			case READABLE:
				return p -> (p.getReadMethod() != null);
			case WRITE_ONLY:
				return p -> (p.getReadMethod() == null) && (p.getWriteMethod() != null);
			case WRITEABLE:
				return p -> (p.getWriteMethod() != null);
		}
		throw new IllegalArgumentException();
	}

	public static enum Accessibility {
		READ_ONLY, WRITE_ONLY, READABLE, WRITEABLE, READ_WRITE, ANY;
	}
}
