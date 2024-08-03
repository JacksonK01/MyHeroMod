package net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import org.jetbrains.annotations.NotNull;

/**
 * If an ability needs a cooldown, it'll need to implement this
 */
public interface ICooldownAbility extends IActiveAbility {

    @NotNull
    Cooldown getCooldown();

    default void reset() {
        getCooldown().reset();
    }

    default boolean isReady() {
        return getCooldown().isReady();
    }

    default boolean isReadyAndReset() {
        return getCooldown().isReadyAndReset();
    }

}
