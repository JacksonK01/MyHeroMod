package net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IAbility;
/**
 * If an ability needs activation every tick, it'll need to implement this
 */
public interface ITickAbility extends IAbility {

    void onTick();

}
