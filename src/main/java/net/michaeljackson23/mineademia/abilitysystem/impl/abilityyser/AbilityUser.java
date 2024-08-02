package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.impl.Abilities;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityset.AbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbilityUser implements IAbilityUser {

    private final LivingEntity entity;

    private int stamina;

    private boolean enabled;
    private boolean blocked;

    private final AbilityMap abilityMap;

    public AbilityUser(LivingEntity entity) {
        this.entity = entity;

        this.enabled = true;
        this.blocked = false;

        this.abilityMap = new AbilityMap();
    }

    @Override
    public <T extends IActiveAbility> void execute(@NotNull Class<T> type, boolean isKeyDown) {
        IActiveAbility ability = abilityMap.get(type);

        if (ability != null && canExecute(ability))
            ability.execute(isKeyDown);
    }

    @Override
    public @NotNull IAbilityMap getAbilities() {
        return abilityMap;
    }

    @Override
    public void setAbilities(@NotNull IAbilitySet abilities) {
        Abilities.unregisterAbilities(this);
        abilityMap.setAbilities(abilities);
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
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

}
