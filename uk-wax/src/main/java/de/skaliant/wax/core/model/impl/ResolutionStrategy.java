package de.skaliant.wax.core.model.impl;

import de.skaliant.wax.core.model.ControllerInfo;


/**
 * Interface unifying different strategies used for finding controllers.
 *
 * @author Udo Kastilan
 */
interface ResolutionStrategy {
	/**
	 * Find a controller for a name. Result may be <code>null</code>.
	 * 
	 * @param name
	 * @return
	 */
	ControllerInfo findForName(String name);


	/**
	 * Find a controller for its class. Result may be <code>null</code>.
	 * 
	 * @param ctrlClass
	 * @return
	 */
	ControllerInfo findForType(Class<?> ctrlClass);


	/**
	 * Find the default controller which shall be used if no explicity controller
	 * information was given in the call. Result may be <code>null</code>.
	 * 
	 * @return
	 */
	ControllerInfo findDefault();
}
