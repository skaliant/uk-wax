package de.skaliant.wax.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use this annotation to mark a specific action method or the whole controller class (and thus
 * all its actions) as guarded by the given Guardian. Typically, Guardian instanced check for
 * a login status and the like to make sure the request is authorized to call an action. Guardians
 * may for instance redirect to a login action or simply give a HTTP error code.
 *
 * @author Udo Kastilan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Guarded
{
	Class<Guardian> by();
}
