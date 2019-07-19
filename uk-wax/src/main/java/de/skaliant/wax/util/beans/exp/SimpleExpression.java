package de.skaliant.wax.util.beans.exp;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;


/**
 * 
 *
 * @author Udo Kastilan
 */
class SimpleExpression extends AbstractBeanExpression {
	private String name;


	SimpleExpression(String name) {
		this.name = name;
	}


	String getName() {
		return name;
	}


	@Override
	public boolean isIndexed() {
		return false;
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

		try {
			return pd.getReadMethod().invoke(bean);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error reading property \"" + name
					+ "\" on bean of type " + bean.getClass().getName(), ex);
		}
	}


	@Override
	public void writeTo(Object bean, Object value) {
		PropertyDescriptor pd = findReadableProperty(bean.getClass(), name);

		try {
			pd.getWriteMethod().invoke(bean, value);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error when writing property \"" + name
					+ "\" on bean of type " + bean.getClass().getName(), ex);
		}
	}
}
