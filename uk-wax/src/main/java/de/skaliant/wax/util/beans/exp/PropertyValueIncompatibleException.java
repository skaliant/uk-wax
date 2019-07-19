package de.skaliant.wax.util.beans.exp;

/**
 * 
 *
 * @author Udo Kastilan
 */
public class PropertyValueIncompatibleException extends PropertyException {
	private Class<?> valueType;


	protected PropertyValueIncompatibleException(Class<?> beanClass,
			String propertyName, Class<?> valueType) {
		super(beanClass, propertyName);
		this.valueType = valueType;
	}


	public Class<?> getValueType() {
		return valueType;
	}
}
