package net.michaeljackson23.mineademia.abilitysystem.intr.ability.passive;

import net.fabricmc.fabric.api.event.Event;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IPassiveAbility;

/**
 * Base class of all scheduled passive abilities
 * MAKE SURE TO REGISTER THE DESIRED EVENTS AND TRIGGER THE PASSIVE EVENT!!!
 */
public interface IEventPassiveAbility<T> extends IPassiveAbility {

    Class<T> getEventType();

    default <T1> boolean tryTriggerEvent(Class<T1> type) {
        if (type.equals(getEventType())) {
            execute(true);
            return true;
        }

        return false;
    }

}
