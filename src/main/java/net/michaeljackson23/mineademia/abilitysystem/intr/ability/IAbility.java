package net.michaeljackson23.mineademia.abilitysystem.intr.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all abilities
 */
public interface IAbility {

    @NotNull
    IAbilityUser getUser();

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    void execute();

    boolean isActive();
    void setActive(boolean active);

    default LivingEntity getEntity() {
        return getUser().getEntity();
    }

}
