package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to mark methods as exit methods. These methods will be called after
 * the action method has finished. Can be used to clean up.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Exit
{
	//
}
