package net.michaeljackson23.mineademia.abilitysystem.intr.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all abilities
 */
public interface IAbility {

    @NotNull
    IAbilityUser getUser();

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    void execute(boolean isKeyDown);
    void cancel();

    boolean isActive();
    void setActive(boolean active);

    default LivingEntity getEntity() {
        return getUser().getEntity();
    }

    default void offsetStamina(int stamina) {
        getUser().offsetStamina(stamina);
    }

    default int getStamina() {
        return getUser().getStamina();
    }

    default boolean hasStamina(int stamina) {
        return getUser().hasStamina(stamina);
    }

    default boolean hasStaminaAndConsume(int stamina) {
        if (getUser().hasStamina(stamina)) {
            getUser().offsetStamina(-stamina);
            return true;
        }

        return false;
    }

    default boolean canExecute() {
        return true;
    }

}
