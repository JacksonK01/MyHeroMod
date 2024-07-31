package net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

/**
 * If an ability is cancelable, it'll need to implement this
 */
public interface ICancelableAbility extends IActiveAbility {

    void cancel(@NotNull IAbilityUser user);

}
