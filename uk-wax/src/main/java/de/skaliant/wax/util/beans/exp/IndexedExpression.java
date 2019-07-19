package de.skaliant.wax.util.beans.exp;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

import de.skaliant.wax.util.TypeUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
class IndexedExpression extends AbstractBeanExpression {
	private String name;
	private int index;


	IndexedExpression(String name, int index) {
		this.name = name;
		this.index = index;
	}


	String getName() {
		return name;
	}


	int getIndex() {
		return index;
	}


	@Override
	public boolean isIndexed() {
		return true;
	}


	@Override
	public Type getTypeIn(Object bean) {
		PropertyDescriptor pd = findProperty(bean.getClass(), name);

		if (pd.getReadMethod() != null) {
			return pd.getReadMethod().getGenericReturnType();
		}

		return pd.getWriteMethod().getGenericParameterTypes()[0];
	}


	@Override
	public Object readFrom(Object bean) {
		PropertyDescriptor pd = findReadableProperty(bean.getClass(), name);
		Object container = null;
		Object result = null;

		try {
			container = pd.getReadMethod().invoke(bean);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error reading property \"" + name
					+ "\" on bean of type " + bean.getClass().getName(), ex);
		}
		if (container instanceof List) {
			result = getElementFromList((List<?>) container, index);
		} else if ((container != null) && container.getClass().isArray()) {
			result = getElementFromArray(container, index);
		}

		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void writeTo(Object bean, Object value) {
		PropertyDescriptor pd = findReadableProperty(bean.getClass(), name);
		Object container;

		try {
			container = pd.getReadMethod().invoke(bean);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error reading property \"" + name
					+ "\" on bean of type " + bean.getClass().getName(), ex);
		}
		if (container instanceof List) {
			assertTypeCompatible(bean.getClass(), pd, value);
			setElementOnList((List<Object>) container, index, value);
		} else if ((container != null) && container.getClass().isArray()) {
			setElementOnArray(container, index, value);
		}
	}


	private static void assertTypeCompatible(Class<?> beanClass,
			PropertyDescriptor prop, Object value) {
		if (value != null) {
			Type listType = TypeUtils
					.getFirstGenericArgument(prop.getReadMethod().getGenericReturnType());

			if (listType != null) {
				Class<?> raw = TypeUtils.getRawType(listType);

				if (!raw.isAssignableFrom(value.getClass())) {
					throw new PropertyValueIncompatibleException(beanClass,
							prop.getName(), value.getClass());
				}
			}
		}
	}


	private static Object getElementFromArray(Object array, int index) {
		int len = Array.getLength(array);

		if (len > index) {
			return Array.get(array, index);
		}

		return null;
	}


	private static Object getElementFromList(List<?> list, int index) {
		if (list.size() > index) {
			return list.get(index);
		}

		return null;
	}


	private static void setElementOnArray(Object array, int index, Object value) {
		int len = Array.getLength(array);

		if (len > index) {
			Array.set(array, index, value);
		}
	}


	private static <T> void setElementOnList(List<T> list, int index, T value) {
		while (list.size() < (index + 1)) {
			list.add(null);
		}
		list.set(index, value);
	}
}
