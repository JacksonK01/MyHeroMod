package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.fiercewings.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public class FeatherSensesAbility extends DataPassiveAbility {

    public static final String DESCRIPTION = "";

    public static final float MAX_RANGE = 50;


    public FeatherSensesAbility(@NotNull IAbilityUser user) {
        super(user, "Feather Senses", DESCRIPTION);
    }

}
