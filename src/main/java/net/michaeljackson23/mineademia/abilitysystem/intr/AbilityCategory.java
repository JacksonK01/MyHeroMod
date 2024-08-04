package net.michaeljackson23.mineademia.abilitysystem.intr;

import org.jetbrains.annotations.NotNull;

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

}
