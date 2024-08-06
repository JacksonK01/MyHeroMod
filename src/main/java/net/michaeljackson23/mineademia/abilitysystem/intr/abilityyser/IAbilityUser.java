package net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Base class for all ability users
 */
public interface IAbilityUser {

    @NotNull
    LivingEntity getEntity();

    @NotNull
    IAbilityMap getAbilities();
    void setAbilities(@NotNull IAbilitySet abilities);

    @SuppressWarnings("unchecked")
    default void setAbilities(@NotNull Function<IAbilityUser, IAbilitySet>... abilityCreators) {
        List<IAbilitySet> sets = Stream.of(abilityCreators).map((a) -> a.apply(this)).toList();
        setAbilities(new AbilitySet(sets));
    }

    @Nullable
    <T extends IAbility> T getAbility(@NotNull Class<T> type);

    <T extends IActiveAbility> void execute(@NotNull Class<T> type, boolean isKeyDown);

    default boolean canExecute(@NotNull IActiveAbility ability) {
        return !isBlocked() && isEnabled() && ability.isActive() && ability.canExecute();
    }

    int getMaxStamina();
    int getStamina();

    default boolean hasStamina(int stamina) {
        return getStamina() >= stamina;
    }

    void setStamina(int amount);
    default void offsetStamina(int offset) {
        setStamina(getStamina() + offset);
    }

    int getStaminaRegenAmount();
    int getStaminaRegenRate();

    default boolean onStaminaRegen() { return true; }

    boolean isEnabled();
    boolean isBlocked();

    void setEnabled(boolean enabled);
    void setBlocked(boolean blocked);

}
