package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to explicitly mark a class as a controller class.
 * Can also be used to give a specific name.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller
{
	String value() default "";
}
