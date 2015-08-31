package de.skaliant.wax.core.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Default;
import de.skaliant.wax.app.Exit;
import de.skaliant.wax.app.Guarded;
import de.skaliant.wax.app.Init;
import de.skaliant.wax.util.logging.Log;


/**
 * A class responsible for analyzing controller classes and collecting all information
 * found into an instance of <code>ControllerInfo</code>.
 *
 * @author Udo Kastilan
 */
public class ControllerInspector
{
	private final static String CONTROLLER_SUFFIX = "Controller";
	private final static String ACTION_SUFFIX = "Action";
	private final static String DEFAULT_ACTION_NAME = "index";
	
	private final Log LOG = Log.get(ControllerInspector.class);

	
	/**
	 * Interpret the given class as a controller and analyze it using the specified working mode.
	 * 
	 * @param cls Controller class
	 * @param mode Working mode
	 * @return Found information, or null if it may not be a controller (depending on the working mode)
	 */
	public ControllerInfo inspect(Class<?> cls, ResolutionMode mode)
	{
		List<ActionInfo> aix = new ArrayList<ActionInfo>();
		Controller anno = cls.getAnnotation(Controller.class);
		ControllerInfo ci = null;
		boolean hasDefaultAction = false;
		/*
		 * In strict mode, controllers must be annotated
		 */
		if ((mode == ResolutionMode.STRICT) && (anno == null))
		{
			return null;
		}
		ci = new ControllerInfo();
		ci.setType(cls);
		if ((anno != null) && (anno.value().length() != 0))
		{
			ci.setName(anno.value().toLowerCase());
		}
		else
		{
			/*
			 * Generic Controller name: simple class name without "Controller" suffix,
			 * if present
			 */
			String ctrlName = cls.getSimpleName();

			if (ctrlName.endsWith(CONTROLLER_SUFFIX))
			{
				ctrlName = ctrlName.substring(0,
						ctrlName.length() - CONTROLLER_SUFFIX.length());
			}
			ci.setName(ctrlName.toLowerCase());
		}
		/*
		 * Guarded controller?
		 */
		if (cls.isAnnotationPresent(Guarded.class))
		{
			ci.setGuardian(cls.getAnnotation(Guarded.class).by());
		}
		/*
		 * Annotated as default controller?
		 */
		ci.setDefaultController(cls.isAnnotationPresent(Default.class));
		/*
		 * Check methods for being action, init, or exit methods; init and exit
		 * methods must not have parameters and must be void
		 */
		for (Method meth : cls.getMethods())
		{
			boolean initOrExit = false;

			if (meth.isAnnotationPresent(Init.class)
					&& (meth.getParameterTypes().length == 0)
					&& (meth.getReturnType() == Void.TYPE))
			{
				ci.addInitMethod(meth);
				initOrExit = true;
			}
			if (meth.isAnnotationPresent(Exit.class)
					&& (meth.getParameterTypes().length == 0)
					&& (meth.getReturnType() == Void.TYPE))
			{
				ci.addExitMethod(meth);
				initOrExit = true;
			}
			if (!initOrExit)
			{
				ActionInfo ai = inspect(meth, mode);

				if (ai != null)
				{
					aix.add(ai);
					if (ai.isDefaultAction())
					{
						if (hasDefaultAction)
						{
							LOG.error("Controller [" + cls.getName() + "] has more than one default action, ignoring any but the first one");
							ai.setDefaultAction(false);
						}
						else
						{
							hasDefaultAction = true;
						}
					}
				}
			}
		}
		/*
		 * Check for default action
		 */
		if (!hasDefaultAction)
		{
			if (aix.size() == 1)
			{
				aix.get(0).setDefaultAction(true);
			}
			else
			{
				for (ActionInfo ai : aix)
				{
					if (ai.getName().equalsIgnoreCase(DEFAULT_ACTION_NAME))
					{
						ai.setDefaultAction(true);
						break;
					}
				}
			}
		}
		/*
		 * Add actions to controller
		 */
		for (ActionInfo ai : aix)
		{
			ci.add(ai);
		}
		return ci;
	}


	/**
	 * Interpret the given method as a controller action, analyze it using the specified working
	 * mode, and return all information found as an instance of ActionInfo.
	 * 
	 * @param meth Action method
	 * @param mode Working mode
	 * @return Information found, or null if it may not be an action method (depending on the working mode) 
	 */
	public ActionInfo inspect(Method meth, ResolutionMode mode)
	{
		Action anno = meth.getAnnotation(Action.class);
		ActionInfo info = new ActionInfo();

		if ((mode == ResolutionMode.STRICT) && (anno == null))
		{
			return null;
		}
		info = new ActionInfo();
		if ((anno != null) && (anno.value().length() != 0))
		{
			info.setName(anno.value().toLowerCase());
		}
		else
		{
			String n = meth.getName();
			
			if (n.endsWith(ACTION_SUFFIX) && (n.length() > ACTION_SUFFIX.length()))
			{
				n = n.substring(0, n.lastIndexOf(ACTION_SUFFIX));
			}
			info.setName(n.toLowerCase());
		}
		if (meth.isAnnotationPresent(Guarded.class))
		{
			info.setGuardian(meth.getAnnotation(Guarded.class).by());
		}
		info.setDefaultAction(meth.isAnnotationPresent(Default.class));
		info.setMethod(meth);

		return info;
	}
}
