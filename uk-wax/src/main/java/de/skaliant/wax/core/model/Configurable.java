package de.skaliant.wax.core.model;


/**
 * Interface for types which are configurable by providing a config bean.
 *
 * @author Udo Kastilan
 * @param <C> Type of the config bean
 */
public interface Configurable<C>
{
	/**
	 * Get the config bean, must not be null.
	 * 
	 * @return Config bean
	 */
	C getConfig();
}
