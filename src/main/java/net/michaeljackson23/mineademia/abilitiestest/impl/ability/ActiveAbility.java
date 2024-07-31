package net.michaeljackson23.mineademia.abilitiestest.impl.ability;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public abstract class ActiveAbility extends Ability implements IActiveAbility {

    private final HashSet<AbilityCategory> categories;

    public ActiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        super(user, name, description);
        this.categories = new HashSet<>(List.of(categories));
    }

    @Override
    public @NotNull HashSet<AbilityCategory> getCategories() {
        return categories;
    }

}
