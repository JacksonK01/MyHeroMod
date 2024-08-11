package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IHoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class HoldAbility extends ActivationAbility implements IHoldAbility {

    public HoldAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, @NotNull AbilityCategory @NotNull... categories) {
        super(user, name, description, defaultIdentifier, categories);
    }
    public HoldAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        this(user, name, description, null, categories);
    }

}
