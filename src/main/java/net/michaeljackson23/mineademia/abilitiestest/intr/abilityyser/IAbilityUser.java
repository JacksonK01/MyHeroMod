package net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

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
    IAbilitySet getAbilities();
    void setAbilities(@NotNull IAbilitySet abilities);

    default void setAbilities(@NotNull Function<IAbilityUser, IAbilitySet>... abilityCreators) {
        List<IAbilitySet> sets = Stream.of(abilityCreators).map((a) -> a.apply(this)).toList();
        setAbilities(new AbilitySet(sets));
    }

    <T extends IActiveAbility> void execute(@NotNull Class<T> type);

    default boolean canExecute(@NotNull IActiveAbility ability) {
        return !isForcedOff() && isActive() && ability.isActive();
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
