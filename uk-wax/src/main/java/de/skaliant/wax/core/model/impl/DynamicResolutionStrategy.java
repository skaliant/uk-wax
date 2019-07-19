package de.skaliant.wax.core.model.impl;

import static de.skaliant.wax.util.MiscUtils.firstLetterUpperCase;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.util.logging.Log;


/**
 * Strategy for finding controllers dynamically even after initialization has
 * been processed. In this case, controllers don't have to be stated in the
 * configuration explicity, they are searched for when called. This is an easy
 * yet potentially dangerous mode as external information can be used to
 * instantiate and call arbitrary classes in the specfied controller packages.
 * Use it if you know what you're doing and make sure not to place classes into
 * these packages that are not controllers. At least make sure they are not
 * public or don't have public methods. This strategy extends the default one by
 * widening its resolution functions.
 *
 * @author Udo Kastilan
 */
class DynamicResolutionStrategy implements ResolutionStrategy {
	private final static ControllerInfo NOT_FOUND = new ControllerInfo(null,
			null);
	private final Log LOG = Log.get(getClass());
	private Map<String, ControllerInfo> ctrlMap = Collections.emptyMap();
	private Collection<String> configuredPackages;
	private ControllerInfo defaultController = null;


	DynamicResolutionStrategy(Collection<String> configuredPackages,
			Collection<String> configuredControllers) {
		this.configuredPackages = configuredPackages;
		init(configuredPackages, configuredControllers);
	}


	@Override
	public ControllerInfo findForName(String name) {
		String normalized = name.toLowerCase();
		ControllerInfo hit = ctrlMap.get(normalized);

		if (hit == null) {
			hit = findAndRegisterDynamic(name);
		} else if (hit == NOT_FOUND) {
			hit = null;
		}

		return hit;
	}


	@Override
	public ControllerInfo findForType(Class<?> ctrlClass) {
		ControllerInfo hit = ctrlMap.values().stream()
				.filter(c -> c.getType().equals(ctrlClass)).findFirst().orElse(null);

		if (hit == null) {
			hit = ResolutionStrategyUtils.inspectControllerClass(ctrlClass,
					m -> true);
			if (hit != null) {
				String name = hit.getName();

				if (ctrlMap.containsKey(name)) {
					hit = null;
					LOG.warn("Inspection of controller type [" + ctrlClass.getName()
							+ "] failed, another controller is already registered by the name \""
							+ name + '"');
				}
			}
		}

		return hit;
	}


	@Override
	public ControllerInfo findDefault() {
		ControllerInfo hit = defaultController;

		if (hit == null) {
			hit = findAndRegisterDynamic(
					ResolutionStrategyUtils.getDefaultControllerName());
			if (hit != null) {
				defaultController.setDefaultController(true);
			} else {
				defaultController = NOT_FOUND;
			}
		} else if (hit == NOT_FOUND) {
			hit = null;
		}

		return hit;
	}


	private ControllerInfo findAndRegisterDynamic(String name) {
		ControllerInfo hit = null;

		for (String n : createPossibleControllerNameVariants(name)) {
			LOG.info("Trying [" + n + "]");
			hit = findDynamic(n);
			if (hit != null) {
				String ownName = hit.getName();

				register(name, hit);
				if (!name.equals(ownName)) {

				}
				break;
			}
		}

		if (hit == null) {
			LOG.warn("No controller found for name [" + name + "]");
			/*
			 * Make sure we search each name only once
			 */
			register(name, NOT_FOUND);
		}

		return hit;
	}


	private ControllerInfo findDynamic(String name) {
		List<Class<?>> classes = ResolutionStrategyUtils
				.findPossibleControllerClasses(configuredPackages, asList(name));
		ControllerInfo hit = null;

		if (!classes.isEmpty()) {
			Class<?> cls = classes.get(0);

			hit = ResolutionStrategyUtils.inspectControllerClass(cls, m -> true);
			if (hit != null) {
				String ownName = hit.getName();

				register(name, hit);
				if (!name.equals(ownName)) {
					register(ownName, hit);
				}
			} else {
				LOG.warn("Inspection of class " + cls.getName() + " did not succeed");
			}
		}

		return hit;
	}


	private void init(Collection<String> configuredPackages,
			Collection<String> configuredControllers) {
		if (configuredPackages.isEmpty() && configuredControllers.isEmpty()) {
			LOG.warn("Neither packages nor controller classes configured");
			return;
		}
		List<ControllerInfo> result = ResolutionStrategyUtils
				.inspectControllerClasses(configuredPackages, configuredControllers,
						c -> true, m -> true);

		defaultController = result.stream()
				.filter(ControllerInfo::isDefaultController).findFirst().orElse(null);
		if (defaultController == null) {
			defaultController = ResolutionStrategyUtils
					.findDefaultControllerByName(result);
			if (defaultController != null) {
				defaultController.setDefaultController(true);
			}
		}

		ctrlMap = new HashMap<>(
				result.stream().collect(toMap(c -> c.getName(), c -> c)));
	}


	private void register(String name, ControllerInfo ci) {
		if (ctrlMap.containsKey(name)) {
			LOG.warn("Name \"" + name + "\" already taken, controller ["
					+ ci.getType().getName() + "] will not be registered by that name");
		} else {
			ctrlMap.put(name, ci);
		}
	}


	private static String[] createPossibleControllerNameVariants(String name) {
		// @formatter:off
		String[] variants = { 
			firstLetterUpperCase(name) + ResolutionStrategyUtils.getControllerSuffix(),
			firstLetterUpperCase(name) 
		};
		// @formatter:on
		return variants;
	}
}
