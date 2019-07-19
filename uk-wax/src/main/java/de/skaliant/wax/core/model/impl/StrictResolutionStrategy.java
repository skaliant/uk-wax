package de.skaliant.wax.core.model.impl;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.app.Action;
import de.skaliant.wax.app.Controller;
import de.skaliant.wax.app.Exit;
import de.skaliant.wax.app.Init;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.util.logging.Log;


/**
 * Strict resolution strategy. It extends the default strategy through checks on
 * the class and method annotations.
 *
 * @author Udo Kastilan
 */
class StrictResolutionStrategy implements ResolutionStrategy {
	private final Log LOG = Log.get(getClass());
	private Map<String, ControllerInfo> ctrlMap = Collections.emptyMap();
	private ControllerInfo defaultController = null;


	StrictResolutionStrategy(Collection<String> configuredPackages,
			Collection<String> configuredControllers) {
		init(configuredPackages, configuredControllers);
	}


	@Override
	public ControllerInfo findForName(String name) {
		ControllerInfo hit;

		LOG.info("Asking for controller named [" + name + "]");
		hit = ctrlMap.get(name.toLowerCase());
		if (hit == null) {
			LOG.warn("Controller not found: [" + name + "]");
		}

		return hit;
	}


	@Override
	public ControllerInfo findForType(Class<?> ctrlClass) {
		return ctrlMap.values().stream().filter(c -> c.getType().equals(ctrlClass))
				.findFirst().orElse(null);
	}


	@Override
	public ControllerInfo findDefault() {
		return defaultController;
	}


	private void init(Collection<String> configuredPackages,
			Collection<String> configuredControllers) {
		if (configuredPackages.isEmpty() && configuredControllers.isEmpty()) {
			LOG.warn("Neither packages nor controller classes configured");
			return;
		}
		List<ControllerInfo> result = ResolutionStrategyUtils
				.inspectControllerClasses(configuredPackages, configuredControllers,
						this::isClassInspectable, this::isMethodInspectable);

		defaultController = result.stream()
				.filter(ControllerInfo::isDefaultController).findFirst().orElse(null);
		ctrlMap = result.stream().collect(toMap(c -> c.getName(), c -> c));
	}


	private boolean isClassInspectable(Class<?> c) {
		return c.isAnnotationPresent(Controller.class);
	}


	private boolean isMethodInspectable(Method m) {
		return m.isAnnotationPresent(Action.class)
				|| m.isAnnotationPresent(Init.class)
				|| m.isAnnotationPresent(Exit.class);
	}
}
