package net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;

/**
 * Interface of right click abilities
 */
public interface IRightClickAbility extends IActiveAbility {

    boolean onRightClick(boolean isKeyDown);

}
