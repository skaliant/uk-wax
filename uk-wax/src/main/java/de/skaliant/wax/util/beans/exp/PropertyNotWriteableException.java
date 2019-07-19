package de.skaliant.wax.util.beans.exp;

/**
 * 
 *
 * @author Udo Kastilan
 */
public class PropertyNotWriteableException extends PropertyException {
	public PropertyNotWriteableException(Class<?> beanClass,
			String propertyName) {
		super(beanClass, propertyName);
	}
}
