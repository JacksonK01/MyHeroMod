package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IActivationAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class ActivationAbility extends ActiveAbility implements IActivationAbility {

    private boolean active;
    private int ticks;

    public ActivationAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        super(user, name, description, categories);

        this.active = false;
        this.ticks = 0;
    }


    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public void setTicks(int ticks) {
        this.ticks = Math.max(-1, ticks);
    }

}
