package net.michaeljackson23.mineademia.abilitysystem.impl.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public abstract class ActiveAbility extends Ability implements IActiveAbility {

    private final HashSet<AbilityCategory> categories;
    private final HashSet<AbilityCategory> blockingState;

    public ActiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        super(user, name, description);

        this.categories = new HashSet<>(List.of(categories));
        this.blockingState = new HashSet<>();
    }

    @Override
    public @NotNull HashSet<AbilityCategory> getCategories() {
        return categories;
    }

    @Override
    public @NotNull HashSet<AbilityCategory> getBlockingState() {
        return blockingState;
    }

    @Override
    public void setBlockingState(@NotNull AbilityCategory... categories) {
        blockingState.clear();
        blockingState.addAll(List.of(categories));
    }

}
