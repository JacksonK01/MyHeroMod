package net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
/**
 * If an ability needs activation every tick, it'll need to implement this
 */
public interface ITickAbility extends IAbility {

    void onStartTick();

    default void onEndTick() { }

}
