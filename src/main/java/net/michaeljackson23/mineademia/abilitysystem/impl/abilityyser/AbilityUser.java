package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityset.AbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IBlockReason;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class AbilityUser implements IAbilityUser {

    private LivingEntity entity;

    private int stamina;

    private boolean enabled;
    private final HashSet<IBlockReason> blockReasons;

    private final IAbilityMap abilityMap;

    public AbilityUser(@NotNull LivingEntity entity) {
        this.entity = entity;

        this.enabled = true;
        this.blockReasons = new HashSet<>();

        this.abilityMap = new AbilityMap();
    }

    @Override
    public int getMaxStamina() {
        return 1000;
    }

    @Override
    public int getStaminaRegenAmount() {
        return 1;
    }

    @Override
    public int getStaminaRegenRate() {
        return 1;
    }

    @Override
    public <T extends IActiveAbility> void execute(@NotNull Class<T> type, boolean isKeyDown) {
        IActiveAbility ability = getAbility(type);

        if (ability != null && canExecute(ability))
            ability.execute(isKeyDown);
    }

    @Override
    public @NotNull IAbilityMap getAbilities() {
        return abilityMap;
    }

    @Override
    public void setAbilities(@NotNull IAbilitySet abilities) {
        AbilityManager.unregisterAbilities(this);
        abilityMap.setAbilities(abilities);
        AbilityManager.registerAbilities(this);
    }

    @Override
    public <T extends IAbility> @Nullable T getAbility(@NotNull Class<T> type) {
        return getAbilities().get(type);
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public void setStamina(int amount) {
        this.stamina = Mathf.clamp(0, getMaxStamina(), amount);
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
        return !blockReasons.isEmpty();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        cancelAbilities();
    }

    @Override
    public boolean addBlockReason(@NotNull IBlockReason reason, boolean cancel) {
        boolean result = blockReasons.add(reason);

        if (cancel)
            cancelAbilities();

        return result;
    }

    @Override
    public boolean removeBlockReason(@NotNull IBlockReason reason) {
        return blockReasons.remove(reason);
    }

    public void setEntity(@NotNull LivingEntity entity) {
        this.entity = entity;
    }

    private void cancelAbilities() {
        if (!this.isEnabled() || this.isBlocked()) {
            for (IAbility ability : abilityMap.values())
                ability.cancel();
        }
    }

}
