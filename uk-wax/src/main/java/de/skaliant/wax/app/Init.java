package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to mark methods as init methods. These methods will be invoked
 * before the action method is called. There is no guarantee on a certain order, so
 * either don't rely on any order, or combine any initialisation needs into a single init
 * method.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Init
{
	//
}
