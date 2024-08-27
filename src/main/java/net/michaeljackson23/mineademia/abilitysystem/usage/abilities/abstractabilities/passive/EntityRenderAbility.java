package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.TogglePassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class EntityRenderAbility extends TogglePassiveAbility {

    private float range;

    public EntityRenderAbility(@NotNull IAbilityUser user, @NotNull String description) {
        super(user, "Entity Render", description);
        this.range = -1;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

}
