package net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all ability users
 */
public interface IAbilityUser {

    @NotNull
    LivingEntity getEntity();

    @NotNull
    IAbilitySet getAbilitySet();

    <T extends IActiveAbility> void execute(@NotNull Class<T> type);

    default boolean canExecute(@NotNull IActiveAbility ability) {
        return !isForcedOff() && isActive() && getAbilitySet().contains(ability) && ability.isActive();
    }

    int getMaxStamina();
    int getStamina();

    void setStamina(int amount);
    default void offsetStamina(int offset) {
        setStamina(getStamina() + offset);
    }

    boolean isActive();
    boolean isForcedOff();

    void setActive(boolean active);
    void setForcedOff(boolean forcedOff);

}
