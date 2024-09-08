package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.AbilityBlockReason;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class SuperchargedShotAbility extends ToggleAbility {

    public static final String DESCRIPTION = "";

    public static final int STAMINA_COST = 200;

    public static final float DAMAGE_MULTIPLIER = 2.5f;
    public static final float TICKS_TO_TARGET_MULTIPLIER = 1.5f;
    public static final float PIERCE_MULTIPLIER = 1.2f;

    public static final int JAMMING_TIME = 200;
    public static final float JAMMING_CHANCE = 0.2f;


    private int jammedTime;

    private final AbilityBlockReason blockReason;

    public SuperchargedShotAbility(@NotNull IAbilityUser user) {
        super(user, "Supercharged Shot", DESCRIPTION, Networking.C2S_ABILITY_FOUR, AbilityCategory.UTILITY);

        this.jammedTime = -1;

        this.blockReason = new AbilityBlockReason(this);
    }

    // Inform player in nicer ways
    @Override
    public boolean executeStart() {
        getEntity().sendMessage(Text.literal("SuperShot - ON"));
        return true;
    }

    @Override
    public boolean executeEnd() {
        getEntity().sendMessage(Text.literal("SuperShot - OFF"));
        return true;
    }

    @Override
    public void onTickActive() { }

    @Override
    public void onTickInactive() {
        if (jammedTime > 0)
            jammedTime--;

        if (jammedTime == 0) {
            getUser().removeBlockReason(blockReason);
            jammedTime--;
        }
    }

    @Override
    public boolean tickOnBlock() {
        return true;
    }

    public float getDamage(@NotNull LoadAmmoAbility.AmmoType ammoType) {
        float multiplier = isActive() ? DAMAGE_MULTIPLIER : 1;
        return ammoType.getDamage() * multiplier;
    }

    public int getTicksToTarget(@NotNull LoadAmmoAbility.AmmoType ammoType) {
        float multiplier = isActive() ? TICKS_TO_TARGET_MULTIPLIER : 1;
        return (int) (ammoType.getTicksToHit() * multiplier);
    }

    public byte getPierce(@NotNull LoadAmmoAbility.AmmoType ammoType) {
        float multiplier = isActive() ? PIERCE_MULTIPLIER : 1;
        return (byte) (ammoType.getPierceLevel() * multiplier);
    }

    public int getJammingTime() {
        return jammedTime;
    }

    public boolean isJammed() {
        return jammedTime > 0;
    }

    public boolean tryShootSupercharge() {
        if (!isActive())
            return true;

        if (Math.random() >= JAMMING_CHANCE || !hasStaminaAndConsume(STAMINA_COST)) {
            jammedTime = JAMMING_TIME;
            setActive(false);

            // setBlockedCategories(false, AbilityCategory.allExcept(AbilityCategory.MOBILITY));
            getUser().addBlockReason(blockReason);
            return false;
        }

        return true;
    }

}
