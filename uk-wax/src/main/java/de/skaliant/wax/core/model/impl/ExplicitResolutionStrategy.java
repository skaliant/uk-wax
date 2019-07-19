package de.skaliant.wax.core.model.impl;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.ResolutionMode;
import de.skaliant.wax.util.logging.Log;


/**
 * Defines the resolution strategy for {@link ResolutionMode#EXPLICIT}, which is
 * the default mode. This mode means that you have to explicitly state in the
 * configuration which classes shall act as controllers.
 *
 * @author Udo Kastilan
 */
class ExplicitResolutionStrategy implements ResolutionStrategy {
	private final Log LOG = Log.get(getClass());
	private Map<String, ControllerInfo> ctrlMap = Collections.emptyMap();
	private ControllerInfo defaultController = null;


	ExplicitResolutionStrategy(Collection<String> configuredPackages,
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


	Map<String, ControllerInfo> getAll() {
		return ctrlMap;
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
			} else if (result.size() == 1) {
				defaultController = result.get(0);
				defaultController.setDefaultController(true);
			}
		}

		ctrlMap = result.stream().collect(toMap(c -> c.getName(), c -> c));
	}
}
