package net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

/**
 * If an ability is cancelable, it'll need to implement this
 */
public interface ICancelableAbility extends IActiveAbility {

    void cancel(@NotNull IAbilityUser user);

}
