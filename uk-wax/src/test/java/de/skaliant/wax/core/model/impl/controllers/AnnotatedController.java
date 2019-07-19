package de.skaliant.wax.core.model.impl.controllers;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Default;
import de.skaliant.wax.app.Exit;
import de.skaliant.wax.app.Init;


/**
 * 
 *
 * @author Udo Kastilan
 */
@Controller("anno")
public class AnnotatedController {

	@Init
	public void initMethod1() {
		//
	}


	@Init
	public void initMethod2() {
		//
	}


	@Exit
	public void exitMethod1() {
		//
	}


	@Exit
	public void exitMethod2() {
		//
	}


	@Action("action1")
	public void someMethod(String arg) {
		//
	}


	@Default
	@Action("action2")
	public void anotherMethod(String arg) {
		//
	}
}
