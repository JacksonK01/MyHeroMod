package net.michaeljackson23.mineademia.abilitysystem.impl.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class PassiveAbility extends Ability implements IPassiveAbility {

    public PassiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description) {
        super(user, name, description);
    }

}
