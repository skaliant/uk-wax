package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation on an action method parameter to give a hint
 * which HTTP parameter should be mapped to this method parameter.
 * Unfortunately, an annotation is needed as the actual method parameter
 * names are not available via reflection.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param
{
	String value();
}
