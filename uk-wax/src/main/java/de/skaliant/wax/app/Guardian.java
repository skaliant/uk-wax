package de.skaliant.wax.app;

import de.skaliant.wax.app.results.Result;
import de.skaliant.wax.core.model.Call;


/**
 * Guards access to a controller or some of its actions.
 *
 * @author Udo Kastilan
 */
public interface Guardian
{
	/**
	 * A guardian gets information on the current call and should either return null (meaning access is fine)
	 * or a Result (such as a Redirect) of its own.
	 * 
	 * @param call The call
	 * @return Result, or null if access is to be granted
	 */
	Result admit(Call call);
}
