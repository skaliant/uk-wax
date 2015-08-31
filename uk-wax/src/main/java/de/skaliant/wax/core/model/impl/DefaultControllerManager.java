package de.skaliant.wax.core.model.impl;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.ControllerInspector;
import de.skaliant.wax.core.model.ControllerManagerConfig;
import de.skaliant.wax.core.model.ResolutionMode;
import de.skaliant.wax.util.MiscUtils;
import de.skaliant.wax.util.logging.Log;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class DefaultControllerManager
	implements ControllerManager
{
	private final static ControllerInfo DUMMY = new ControllerInfo();
	private final static String INDEX_CONTROLLER_NAME = "index";
	private final static String CONTROLLER_SUFFIX = "Controller";
	private final Log LOG = Log.get(DefaultControllerManager.class);

	private ControllerManagerConfig config = new ControllerManagerConfig();
	private Map<String, ControllerInfo> ctrlMap = new HashMap<String, ControllerInfo>();
	private Set<String> packageNames = new HashSet<String>();
	private ControllerInfo defaultController = null;
	private boolean scanned = false;

	
	public ControllerManagerConfig getConfig()
	{
		return config;
	}
	
	
	public ControllerInfo findForName(String name)
	{
		ControllerInfo ci = null;

		ensureInitialized();
		ci = ctrlMap.get(name.toLowerCase());
		LOG.info("Asking for controller named [" + name + "]");
		if (ci == DUMMY)
		{
			ci = null;
		}
		else if (ci != null)
		{
			LOG.info("Direct hit");
		}
		else if ((ci == null) && (config.getMode() == ResolutionMode.DYNAMIC))
		{
			String[] tryThese =
			{
					MiscUtils.firstLetterCase(name) + CONTROLLER_SUFFIX,
					MiscUtils.firstLetterCase(name),
					name
			};
			Class<?> cls = null;
			String prev = "";

			for (String n : tryThese)
			{
				if (n.equals(prev))
				{
					continue;
				}
				LOG.info("Trying [" + n + "]");
				cls = seek(n, packageNames);
				if (cls != null)
				{
					ci = new ControllerInspector().inspect(cls, config.getMode());
					if (ci != null)
					{
						add(ci);
					}
					else
					{
						LOG.warn("Inspection of class " + cls.getName()
								+ " did not succeed");
					}
					break;
				}
				prev = n;
			}
			if (ci == null)
			{
				LOG.warn("No controller found for name [" + name + "]");
				/*
				 * Make sure we search only once for each name
				 */
				add(DUMMY, name.toLowerCase());
			}
		}
		return ci;
	}


	public ControllerInfo findForType(Class<?> type)
	{
		ControllerInfo hit = null;

		ensureInitialized();
		for (ControllerInfo ci : ctrlMap.values())
		{
			if ((ci != DUMMY) && type.equals(ci.getType()))
			{
				hit = ci;
				break;
			}
		}
		if ((hit == null) && (config.getMode() == ResolutionMode.DYNAMIC))
		{
			hit = new ControllerInspector().inspect(type, config.getMode());
			if (hit != null)
			{
				add(hit);
			}
		}
		return hit;
	}
	

	public ControllerInfo findDefaultController()
	{
		ControllerInfo ci = null;

		ensureInitialized();
		ci = defaultController;
		if ((ci == null) && (config.getMode() == ResolutionMode.DYNAMIC))
		{
			ci = findForName(INDEX_CONTROLLER_NAME);
			if (ci != null)
			{
				ci.setDefaultController(true);
				defaultController = ci;
			}
		}
		return ci;
	}


	private void scan(List<String> packages, List<String> controllers)
	{
		Set<String> controllerNames = new HashSet<String>();

		if (packages != null)
		{
			packageNames.addAll(packages);
		}
		if (controllers != null)
		{
			controllerNames.addAll(controllers);
		}
		if (packageNames.isEmpty() && controllerNames.isEmpty())
		{
			LOG.warn("Neither packages nor controller classes configured");
		}
		for (String c : controllerNames)
		{
			Class<?> cls = seek(c, packageNames);

			if (cls != null)
			{
				if (usable(cls))
				{
					ControllerInfo ci = new ControllerInspector().inspect(cls, config.getMode());

					if (ci != null)
					{
						add(ci);
					}
					else
					{
						LOG.warn("Inspection of class " + cls.getName()
								+ " did not succeed");
					}
				}
				else
				{
					LOG.error("Class " + cls.getName()
							+ " has no default constructor, cannot use it");
				}
			}
		}
	}

	
	private void ensureInitialized()
	{
		if (!scanned)
		{
			final String SEPARATORS = " \r\n\t,;";
			
			scan(MiscUtils.split(config.getPackages(), SEPARATORS), MiscUtils.split(config.getClasses(), SEPARATORS));
			scanned = true;
		}
	}
	
	
	private void add(ControllerInfo ci)
	{
		add(ci, ci.getName());
	}

	private void add(ControllerInfo ci, String name)
	{
		String n = name.toLowerCase();

		if (!ctrlMap.containsKey(n))
		{
			if (ci.isDefaultController())
			{
				if (defaultController == null)
				{
					defaultController = ci;
				}
				else
				{
					ci.setDefaultController(false);
					LOG.error("There can only be one default controller; using "
							+ defaultController.getType().getName() + ", also found "
							+ ci.getType().getName());
					ci.setDefaultController(false);
				}
			}
			ctrlMap.put(n, ci);
		}
		else
		{
			LOG.error("Found more than one controller named \"" + n + "\": "
					+ ctrlMap.get(n).getType().getName() + " and "
					+ ci.getType().getName());
		}
	}


	private Class<?> seek(String name, Collection<String> packages)
	{
		Class<?> cls = null;
		/*
		 * Controller name containing dots might be a fully qualified class name
		 */
		if (name.indexOf('.') != -1)
		{
			cls = seek(name);
		}
		/*
		 * If not found, try any combination of packages and class name
		 */
		if (cls == null)
		{
			for (String pn : packages)
			{
				cls = seek(pn + '.' + name);
				if (cls != null)
				{
					LOG.info("Found controller class " + cls.getName());
					break;
				}
			}
		}
		return cls;
	}


	private Class<?> seek(String name)
	{
		Class<?> cls = null;

		try
		{
			cls = Class.forName(name);
		}
		catch (ClassNotFoundException ex)
		{
			LOG.info("Seeking " + name + " was futile");
		}
		return cls;
	}


	private static boolean usable(Class<?> cls)
	{
		for (Constructor<?> ctor : cls.getConstructors())
		{
			if (ctor.getParameterTypes().length == 0)
			{
				return true;
			}
		}
		return false;
	}
}
