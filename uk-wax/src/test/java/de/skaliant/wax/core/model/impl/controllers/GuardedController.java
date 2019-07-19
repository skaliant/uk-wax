package de.skaliant.wax.core.model.impl.controllers;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Default;
import de.skaliant.wax.app.Guarded;


/**
 * 
 *
 * @author Udo Kastilan
 */
@Controller("guarded")
@Default
@Guarded(by = TestGuardian1.class)
public class GuardedController {
	@Default
	@Action("default")
	public void mainMethod() {
		//
	}


	@Action("normal")
	public void otherMethod() {
		//
	}
}
