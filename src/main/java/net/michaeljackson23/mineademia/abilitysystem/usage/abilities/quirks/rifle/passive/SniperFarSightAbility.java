package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.EntityRenderAbility;
import org.jetbrains.annotations.NotNull;

public class SniperFarSightAbility extends EntityRenderAbility {

    public static final String DESCRIPTION = "";


    public SniperFarSightAbility(@NotNull IAbilityUser user) {
        super(user, DESCRIPTION);

        setActive(true);
        setRange(Float.MAX_VALUE);
    }

}
