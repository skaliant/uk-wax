package de.skaliant.wax.util.beans.exp;

import java.beans.PropertyDescriptor;

import de.skaliant.wax.util.beans.Bean;


/**
 * 
 *
 * @author Udo Kastilan
 */
abstract class AbstractBeanExpression implements BeanExpression {

	protected static PropertyDescriptor findProperty(Class<?> beanClass,
			String name) {
		PropertyDescriptor pd = Bean.findPropertyByName(beanClass, name);

		if (pd == null) {
			throw new PropertyNotFoundException(beanClass, name);
		}

		return pd;
	}


	protected static PropertyDescriptor findReadableProperty(Class<?> beanClass,
			String name) {
		PropertyDescriptor pd = findProperty(beanClass, name);

		if (pd.getReadMethod() == null) {
			throw new PropertyNotReadableException(beanClass, name);
		}

		return pd;
	}


	protected static PropertyDescriptor findWriteableProperty(Class<?> beanClass,
			String name) {
		PropertyDescriptor pd = findProperty(beanClass, name);

		if (pd.getWriteMethod() == null) {
			throw new PropertyNotWriteableException(beanClass, name);
		}

		return pd;
	}
}
