package de.skaliant.wax.core.model.impl;

import static de.skaliant.wax.util.MiscUtils.splitAtAnyCharOf;

import java.util.List;

import de.skaliant.wax.core.ControllerManager;
import de.skaliant.wax.core.model.ControllerInfo;
import de.skaliant.wax.core.model.ControllerManagerConfig;


/**
 * 
 * 
 * @author Udo Kastilan
 */
public class DefaultControllerManager implements ControllerManager {
	private ResolutionStrategy resolutionStrategy;
	private ControllerManagerConfig config = new ControllerManagerConfig();


	@Override
	public ControllerManagerConfig getConfig() {
		return config;
	}


	@Override
	public ControllerInfo findForName(String name) {
		return getResolutionStrategy().findForName(name);
	}


	@Override
	public ControllerInfo findForType(Class<?> type) {
		return getResolutionStrategy().findForType(type);
	}


	@Override
	public ControllerInfo findDefaultController() {
		return getResolutionStrategy().findDefault();
	}


	private ResolutionStrategy getResolutionStrategy() {
		if (resolutionStrategy == null) {
			final String SEPARATORS = " \r\n\t,;";
			List<String> ctrlPackages = splitAtAnyCharOf(config.getPackages(),
					SEPARATORS);
			List<String> ctrlClasses = splitAtAnyCharOf(config.getClasses(),
					SEPARATORS);

			switch (getConfig().getMode()) {
				case DYNAMIC:
					resolutionStrategy = new DynamicResolutionStrategy(ctrlPackages,
							ctrlClasses);
					break;
				case STRICT:
					resolutionStrategy = new StrictResolutionStrategy(ctrlPackages,
							ctrlClasses);
					break;
				default:
					resolutionStrategy = new ExplicitResolutionStrategy(ctrlPackages,
							ctrlClasses);
			}
		}

		return resolutionStrategy;
	}
}
