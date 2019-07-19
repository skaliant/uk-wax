package de.skaliant.wax.core.model;

/**
 * Possible working modes for the resolving process of the ControllerManager.
 *
 * @author Udo Kastilan
 */
public enum ResolutionMode {
	/**
	 * Most secure mode: controller classes must be given in the configuration,
	 * and controllers and their actions must be annotated.
	 */
	STRICT,
	/**
	 * Secure mode: Controller classes must be given in the configuration,
	 * annotations are optional.
	 */
	EXPLICIT,
	/**
	 * Convenient mode: Controller packages must be given in the configuration,
	 * and controllers may be resolved dynamically via naming conventions.
	 */
	DYNAMIC;
}
