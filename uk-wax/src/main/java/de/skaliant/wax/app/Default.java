package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to mark a controller class or a controller action
 * as the default controller or default action respectively. A default
 * controller is the one which will be used to handle a request if no
 * path info part is present, or if it cannot be resolved to be a 
 * controller name. A default action is the one which will be used to
 * handle a request if no path info part for an action is present, or if
 * the path info part cannot be resolved into any of the action methods
 * present.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Default
{
	//
}
