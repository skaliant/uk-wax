package de.skaliant.wax.util.beans.exp;

/**
 * 
 *
 * @author Udo Kastilan
 */
public abstract class PropertyException extends RuntimeException {
	private Class<?> beanClass;
	private String propertyName;


	protected PropertyException(Class<?> beanClass, String propertyName) {
		this.beanClass = beanClass;
		this.propertyName = propertyName;
	}


	public Class<?> getBeanClass() {
		return beanClass;
	}


	public String getPropertyName() {
		return propertyName;
	}
}
