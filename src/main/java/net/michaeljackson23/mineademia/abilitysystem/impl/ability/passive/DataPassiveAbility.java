package net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.PassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public class DataPassiveAbility extends PassiveAbility {

    public DataPassiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description) {
        super(user, name, description);
    }

    @Override
    public void execute(boolean isKeyDown) { }

}
