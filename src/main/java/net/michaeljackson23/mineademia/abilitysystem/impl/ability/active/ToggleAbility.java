package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ToggleAbility extends ActivationAbility implements IToggleAbility {

    public ToggleAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, @NotNull AbilityCategory @NotNull... categories) {
        super(user, name, description, defaultIdentifier, categories);
    }
    public ToggleAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        this(user, name, description, null, categories);
    }

}
