package net.michaeljackson23.mineademia.abilitiestest.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.impl.Abilities;
import net.michaeljackson23.mineademia.abilitiestest.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbilityUser implements IAbilityUser {

    private final LivingEntity entity;

    private int stamina;

    private boolean active;
    private boolean forcedOff;

    private IAbilitySet abilities;

    public AbilityUser(LivingEntity entity) {
        this.entity = entity;

        this.active = true;
        this.forcedOff = false;

        this.abilities = new AbilitySet();
    }

    @Override
    public <T extends IActiveAbility> void execute(@NotNull Class<T> type) {
        IActiveAbility ability = getAbilities().getByType(type);
        if (ability != null && canExecute(ability))
            ability.execute();
    }

    @Override
    public @NotNull IAbilitySet getAbilities() {
        return abilities;
    }

    @Override
    public void setAbilities(@NotNull IAbilitySet abilities) {
        Abilities.unregisterAbilities(this);
        this.abilities = abilities;
        Abilities.registerAbilities(this);
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public void setStamina(int amount) {
        this.stamina = Math.max(0, Math.min(getMaxStamina(), amount));
    }

    @Override
    public @NotNull LivingEntity getEntity() {
        return entity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isForcedOff() {
        return forcedOff;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setForcedOff(boolean forcedOff) {
        this.forcedOff = forcedOff;
    }

}
