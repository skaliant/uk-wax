package de.skaliant.wax.util.beans.exp;

public class PropertyNotFoundException extends PropertyException {
	public PropertyNotFoundException(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName);
	}
}