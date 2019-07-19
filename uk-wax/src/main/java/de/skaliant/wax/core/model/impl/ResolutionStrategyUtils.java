package de.skaliant.wax.core.model.impl;

import static de.skaliant.wax.util.MiscUtils.isNotBlank;
import static de.skaliant.wax.util.TypeUtils.collectPublicMethodsTopDown;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Default;
import de.skaliant.wax.app.Exit;
import de.skaliant.wax.app.Guarded;
import de.skaliant.wax.app.Init;
import de.skaliant.wax.core.model.ActionInfo;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.util.TypeUtils;
import de.skaliant.wax.util.logging.Log;


/**
 * 
 *
 * @author Udo Kastilan
 */
abstract class ResolutionStrategyUtils {
	private final static String CONTROLLER_SUFFIX = "Controller";
	private final static String ACTION_SUFFIX = "Action";
	private final static String DEFAULT_ACTION_NAME = "index";
	private final static String DEFAULT_CONTROLLER_NAME = "index";


	static String getDefaultControllerName() {
		return DEFAULT_CONTROLLER_NAME;
	}


	static String getControllerSuffix() {
		return CONTROLLER_SUFFIX;
	}


	static List<ControllerInfo> inspectControllerClasses(
			Collection<String> configuredPackages,
			Collection<String> configuredClasses, Predicate<Class<?>> classFilter,
			Predicate<Method> methodFilter) {
		return inspectControllerClasses(findPossibleControllerClasses(
				configuredPackages, configuredClasses, classFilter), methodFilter);
	}


	static ControllerInfo inspectControllerClass(Class<?> cls,
			Predicate<Method> methodFilter) {
		List<Method> methods = collectPublicMethodsTopDown(cls).stream()
				.filter(methodFilter).collect(toList());
		ControllerInfo ci = new ControllerInfo(cls, getControllerName(cls));
		/*
		 * Guarded controller?
		 */
		if (cls.isAnnotationPresent(Guarded.class)) {
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
		inspectControllerMethods(ci, methods);
		/*
		 * Check whether action methods are present
		 */
		if (ci.getAllActions().isEmpty()) {
			Log.get(ResolutionStrategyUtils.class)
					.warn("Controller [" + ci.getType().getName()
							+ "] contains no action methods, it will be ignored");
			ci = null;
		}

		return ci;
	}


	static List<Class<?>> findPossibleControllerClasses(
			Collection<String> packages, Collection<String> classes) {
		return findPossibleControllerClasses(packages, classes, c -> true);
	}


	static List<Class<?>> findPossibleControllerClasses(
			Collection<String> packages, Collection<String> classes,
			Predicate<Class<?>> classFilter) {
		List<Class<?>> result = new ArrayList<>(classes.size());

		for (String c : classes) {
			Class<?> cls = seekClassNameInPackages(packages, c);

			if (cls == null) {
				Log.get(ResolutionStrategyUtils.class)
						.warn("No controller class found for name \"" + c + '"');
			} else if (!TypeUtils.hasDefaultConstructor(cls)) {
				Log.get(ResolutionStrategyUtils.class).error("Class " + cls.getName()
						+ " will not be active as it has no default constructor");
			} else if (classFilter.test(cls)) {
				result.add(cls);
			}
		}

		return result;
	}


	static ControllerInfo findDefaultControllerByName(
			Collection<ControllerInfo> controllers) {
		return controllers.stream()
				.filter(c -> DEFAULT_CONTROLLER_NAME.equalsIgnoreCase(c.getName()))
				.findFirst().orElse(null);
	}


	static ActionInfo findDefaultActionByName(ControllerInfo ci) {
		return ci.getAllActions().stream()
				.filter(a -> DEFAULT_ACTION_NAME.equalsIgnoreCase(a.getName()))
				.findFirst().orElse(null);
	}


	private static List<ControllerInfo> inspectControllerClasses(
			List<Class<?>> classes, Predicate<Method> methodFilter) {
		Log log = Log.get(ResolutionStrategyUtils.class);
		List<ControllerInfo> result = new ArrayList<>();
		Set<String> namesFound = new HashSet<>();
		long defaultControllerCount = 0;

		for (Class<?> c : classes) {
			ControllerInfo ci = inspectControllerClass(c, methodFilter);

			if (ci != null) {
				String name = ci.getName();

				if (namesFound.contains(name)) {
					log.warn("Duplicate controller name \"" + name + "\" found in ["
							+ ci.getType().getName() + "], this controller will be ignored");
				} else {
					result.add(ci);
					namesFound.add(name);
				}
			}
		}

		defaultControllerCount = result.stream()
				.filter(ControllerInfo::isDefaultController).count();
		if (defaultControllerCount > 1) {
			log.warn("More than 1 default controller found (" + defaultControllerCount
					+ "), ignoring all but the first one");
			result.stream().filter(ControllerInfo::isDefaultController).skip(1)
					.forEach(c -> c.setDefaultController(false));
		}

		return result;
	}


	private static void inspectControllerMethods(ControllerInfo ci,
			List<Method> publicMethods) {
		Log log = Log.get(ResolutionStrategyUtils.class);
		Set<String> namesFound = new HashSet<>();
		List<ActionInfo> aix = new ArrayList<>();
		ActionInfo defaultAction = null;
		long defaultActionCount = 0;

		for (Method m : publicMethods.stream()
				.filter(ResolutionStrategyUtils::isActionMethod).collect(toList())) {
			ActionInfo ai = inspectActionMethod(m);
			String name = ai.getName();

			if (namesFound.contains(name)) {
				log.warn("Duplicate action name \"" + name
						+ "\" found in action method [" + m.getName() + "] of controller ["
						+ ci.getType().getName() + "], this action will be ignored");
			} else {
				aix.add(ai);
				namesFound.add(name);
			}
		}
		/*
		 * Check for default action
		 */
		defaultActionCount = aix.stream().filter(a -> a.isDefaultAction()).count();
		if (defaultActionCount == 0) {
			/*
			 * In case there is no explicit default action available: if there is only
			 * one method, this will be default. Otherwise check for a method named
			 * like the value of DEFAULT_ACTION_NAME and declare it the default one.
			 * If this fails too, then there will be no default action for this
			 * controller ...
			 */
			if (aix.size() == 1) {
				defaultAction = aix.get(0);
			} else {
				defaultAction = aix.stream()
						.filter(a -> DEFAULT_ACTION_NAME.equalsIgnoreCase(a.getName()))
						.findFirst().orElse(null);
			}
		} else {
			defaultAction = aix.stream().filter(a -> a.isDefaultAction()).findFirst()
					.get();
			if (defaultActionCount > 1) {
				log.error("Controller [" + ci.getType().getName()
						+ "] has more than one default action, ignoring any but the first one");
				aix.stream().forEach(a -> a.setDefaultAction(false));
			}
		}
		if (defaultAction != null) {
			defaultAction.setDefaultAction(true);
		}
		/*
		 * Set results
		 */
		ci.setInitMethods(publicMethods.stream()
				.filter(ResolutionStrategyUtils::isInitMethod).collect(toList()));
		ci.setExitMethods(publicMethods.stream()
				.filter(ResolutionStrategyUtils::isExitMethod).collect(toList()));
		ci.setActions(aix.stream().collect(toMap(a -> a.getName(), a -> a)));
	}


	/**
	 * Interpret the given method as a controller action, analyze it using the
	 * specified working mode, and return all information found as an instance of
	 * ActionInfo.
	 * 
	 * @param meth
	 *          Action method
	 * @param mode
	 *          Working mode
	 * @return Information found, or null if it may not be an action method
	 *         (depending on the working mode)
	 */
	private static ActionInfo inspectActionMethod(Method meth) {
		ActionInfo info = new ActionInfo(meth, getActionName(meth));

		if (meth.isAnnotationPresent(Guarded.class)) {
			info.setGuardian(meth.getAnnotation(Guarded.class).by());
		}
		info.setDefaultAction(meth.isAnnotationPresent(Default.class));

		return info;
	}


	private static Class<?> seekClassNameInPackages(Collection<String> packages,
			String name) {
		List<String> possibleNames = new ArrayList<>();
		Class<?> cls = null;
		/*
		 * Controller name containing dots might be a fully qualified class name
		 */
		if (name.indexOf('.') != -1) {
			possibleNames.add(name);
		}
		for (String pn : packages) {
			possibleNames.add(pn + '.' + name);
		}
		for (String n : possibleNames) {
			try {
				cls = Class.forName(n);
			}
			catch (ClassNotFoundException ex) {
				Log.get(ResolutionStrategyUtils.class)
						.info("Tried [" + n + "] - no class found");
			}
			if (cls != null) {
				Log.get(ResolutionStrategyUtils.class)
						.info("Tried [" + n + "] - success");
				break;
			}
		}

		return cls;
	}


	private static String getActionName(Method m) {
		Action anno = m.getAnnotation(Action.class);
		String name = null;

		if ((anno != null) && (anno.value().length() != 0)) {
			name = anno.value().toLowerCase();
		} else {
			String n = m.getName();

			if (n.endsWith(ACTION_SUFFIX) && (n.length() > ACTION_SUFFIX.length())) {
				n = n.substring(0, n.lastIndexOf(ACTION_SUFFIX));
			}
			name = n.toLowerCase();
		}

		return name;
	}


	private static String getControllerName(Class<?> cls) {
		Controller anno = cls.getAnnotation(Controller.class);
		String name = null;

		if ((anno != null) && isNotBlank(anno.value())) {
			name = anno.value().toLowerCase();
		} else {
			/*
			 * Generic Controller name: simple class name without "Controller" suffix,
			 * if present
			 */
			String ctrlName = cls.getSimpleName();

			if (ctrlName.endsWith(CONTROLLER_SUFFIX)) {
				ctrlName = ctrlName.substring(0,
						ctrlName.length() - CONTROLLER_SUFFIX.length());
			}
			name = ctrlName.toLowerCase();
		}

		return name;
	}


	private static boolean isActionMethod(Method method) {
		return (!method.getDeclaringClass().equals(Object.class))
				&& (!isInitMethod(method)) && !isExitMethod(method);
	}


	private static boolean isInitMethod(Method method) {
		return method.isAnnotationPresent(Init.class)
				&& (method.getParameterTypes().length == 0)
				&& (method.getReturnType() == Void.TYPE);
	}


	private static boolean isExitMethod(Method method) {
		return method.isAnnotationPresent(Exit.class)
				&& (method.getParameterTypes().length == 0)
				&& (method.getReturnType() == Void.TYPE);
	}
}
