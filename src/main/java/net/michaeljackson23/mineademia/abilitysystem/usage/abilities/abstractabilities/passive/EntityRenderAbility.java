package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class EntityRenderAbility extends DataPassiveAbility {

    public static final String DESCRIPTION = "";


    private boolean enabled;

    public EntityRenderAbility(@NotNull IAbilityUser user) {
        super(user, "Entity Render", DESCRIPTION);

        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract double getRenderDistance(@NotNull Entity entity);

}
