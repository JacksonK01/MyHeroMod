package net.michaeljackson23.mineademia.abilitiestest.intr.ability;

import net.michaeljackson23.mineademia.abilitiestest.intr.AbilityCategory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Base class of all active abilities
 */
public interface IActiveAbility extends IAbility {

    @NotNull
    HashSet<AbilityCategory> getCategories();

    default boolean isConflicting(@NotNull IActiveAbility ability) {
        return getCategories().stream().anyMatch(ability.getCategories()::contains);
    }

}
