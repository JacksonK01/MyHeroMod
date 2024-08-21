package net.michaeljackson23.mineademia.abilitysystem.intr.ability;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

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


    @Nullable
    static <T extends IAbility> T getAbility(@NotNull Entity entity, @NotNull Class<T> type, boolean exact) {
        if (!(entity instanceof LivingEntity livingEntity))
            return null;

        IAbilityUser user = AbilityManager.getUser(livingEntity);
        if (user == null)
            return null;

        return user.getAbility(type);
    }
    @Nullable
    static <T extends IAbility> T getAbility(@NotNull Entity entity, @NotNull Class<T> type) {
        return IAbility.getAbility(entity, type, false);
    }

    @NotNull
    static <T extends IAbility> HashSet<T> getAbilities(@NotNull Class<T> type, boolean exact) {
        HashSet<T> result = new HashSet<>();
        AbilityManager.getUsers().forEach((u) -> result.addAll(u.getAbilities().getAbilities(type, exact)));

        return result;
    }

}
