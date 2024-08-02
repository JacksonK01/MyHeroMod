package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.ISegmentedAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class SegmentedAbility extends ActiveAbility implements ISegmentedAbility {

    private final int maxCharges;
    private int charges;

    public SegmentedAbility(@NotNull IAbilityUser user,  @NotNull String name, @NotNull String description, int maxCharges, @NotNull AbilityCategory... categories) {
        super(user, name, description, categories);

        this.maxCharges = Math.max(2, maxCharges);
        this.charges = maxCharges;

        getCooldown().setOnReady(this::onCooldownOver);
    }

    @Override
    public int getMaxCharges() {
        return maxCharges;
    }

    @Override
    public int getCharges() {
        return charges;
    }

    @Override
    public void setCharges(int charges) {
        this.charges = Math.max(0, Math.min(maxCharges, charges));
    }

}
