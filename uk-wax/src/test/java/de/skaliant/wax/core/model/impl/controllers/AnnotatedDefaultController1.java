package de.skaliant.wax.core.model.impl.controllers;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Default;


/**
 * 
 *
 * @author Udo Kastilan
 */
@Default
@Controller("index1")
public class AnnotatedDefaultController1 {
	@Default
	@Action("index")
	public void index() {
		//
	}
}
