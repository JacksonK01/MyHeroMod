package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.ISegmentedAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SegmentedAbility extends ActiveAbility implements ISegmentedAbility {

    private final int maxCharges;
    private int charges;

    public SegmentedAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, int maxCharges, @NotNull AbilityCategory @NotNull... categories) {
        super(user, name, description, defaultIdentifier, categories);

        this.maxCharges = Math.max(2, maxCharges);
        this.charges = maxCharges;

        getCooldown().setOnReady(this::onCooldownOver);
    }
    public SegmentedAbility(@NotNull IAbilityUser user,  @NotNull String name, @NotNull String description, int maxCharges, @NotNull AbilityCategory... categories) {
        this(user, name, description, null, maxCharges, categories);
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
        this.charges = Mathf.clamp(0, maxCharges, charges);
    }

}
