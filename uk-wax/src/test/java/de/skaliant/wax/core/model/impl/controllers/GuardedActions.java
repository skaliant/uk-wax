package de.skaliant.wax.core.model.impl.controllers;

import de.skaliant.wax.app.Guarded;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class GuardedActions {
	@Guarded(by = TestGuardian1.class)
	public void guarded1() {
		//
	}


	@Guarded(by = TestGuardian2.class)
	public void guarded2() {
		//
	}


	public void unguarded() {
		//
	}

}
