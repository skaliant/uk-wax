package de.skaliant.wax.util.beans.exp;

import java.lang.reflect.Type;


/**
 * 
 *
 * @author Udo Kastilan
 */
public interface BeanExpression {
	boolean isIndexed();


	Type getTypeIn(Object bean)
		throws PropertyException;


	Object readFrom(Object bean)
		throws PropertyException;


	void writeTo(Object bean, Object value)
		throws PropertyException;
}
