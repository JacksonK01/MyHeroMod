package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.EntityRenderAbility;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityFarsightAbility extends EntityRenderAbility {

    public EntityFarsightAbility(@NotNull IAbilityUser user) {
        super(user);
    }

    @Override
    public double getRenderDistance(@NotNull Entity entity) {
        return 1;
    }

}
