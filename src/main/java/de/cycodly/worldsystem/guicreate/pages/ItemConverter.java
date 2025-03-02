package de.cycodly.worldsystem.guicreate.pages;

import de.cycodly.worldsystem.guicreate.OrcItem;

/**
 * @author Butzlabben
 * @since 21.05.2018
 */
public interface ItemConverter<T> {

    OrcItem convert(T element);

}
