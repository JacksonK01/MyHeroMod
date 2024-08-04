package net.michaeljackson23.mineademia.abilitysystem.intr.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Base class of all active abilities
 */
public interface IActiveAbility extends IAbility {

    @NotNull
    HashSet<AbilityCategory> getCategories();

    @NotNull
    HashSet<AbilityCategory> getBlockingState();
    void setBlockingState(@NotNull AbilityCategory... categories);

    default boolean isConflicting() {
        IAbilityUser user = getUser();

        for (IAbility ability : user.getAbilities().values()) {
            if (!(ability instanceof IActiveAbility activeAbility))
                continue;

            // If any overlap between the blocking state and the categories, is conflicting
            if (getCategories().stream().anyMatch(activeAbility.getBlockingState()::contains))
                return true;
        }

        return false;
    }

    @Override
    default boolean canExecute() {
        return !isConflicting();
    }

}
