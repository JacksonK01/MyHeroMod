package net.michaeljackson23.mineademia.abilitysystem.intr;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Categorize abilities, will be used for conflicts(Might change)
 */
public enum AbilityCategory {

    MOBILITY,
    ATTACK,
    DEFENSE,
    UTILITY,
    ULTIMATE
    ,
    ANIMATION;

    @NotNull
    public static AbilityCategory[] none() {
        return new AbilityCategory[0];
    }

    @NotNull
    public static AbilityCategory[] all() {
        return AbilityCategory.values();
    }

    @NotNull
    public static AbilityCategory[] allExcept(@NotNull AbilityCategory... categories) {
        HashSet<AbilityCategory> categorySet = new HashSet<>(List.of(categories));
        return Arrays.stream(all()).filter(categorySet::contains).toArray(AbilityCategory[]::new);
    }

}
