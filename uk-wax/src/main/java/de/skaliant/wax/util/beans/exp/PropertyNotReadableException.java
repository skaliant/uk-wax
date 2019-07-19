package de.skaliant.wax.util.beans.exp;

/**
 *
 * @author Udo Kastilan
 */
public class PropertyNotReadableException extends PropertyException {
	public PropertyNotReadableException(Class<?> beanClass, String propertyName) {
		super(beanClass, propertyName);
	}
}
