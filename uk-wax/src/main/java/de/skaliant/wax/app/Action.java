package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to explicitly mark a method within a controller as an action.
 * Can also be used to give an action name different from the method name (e.g. if
 * the action name shall be a keyword which is a reserved name in Java).
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action
{
	String value() default "";
}
