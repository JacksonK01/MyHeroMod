package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class SuperchargedShotAbility extends ToggleAbility {

    public static final String DESCRIPTION = "";

    public static final float COST_MULTIPLIER = 4f;
    public static final float DAMAGE_MULTIPLIER = 2.5f;
    public static final float VELOCITY_MULTIPLIER = 1.5f;

    public static final int JAMMING_TIME = 200;
    public static final float JAMMING_CHANCE = 0.2f;


    private int jammedTime;

    public SuperchargedShotAbility(@NotNull IAbilityUser user) {
        super(user, "Supercharged Shot", DESCRIPTION, Networking.C2S_ABILITY_FOUR, AbilityCategory.UTILITY);

        this.jammedTime = -1;
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
            getUser().setBlocked(false);
            jammedTime--;
        }
    }

    public float getCostMultiplier() {
        return isActive() ? COST_MULTIPLIER : 1;
    }

    public float getDamageMultiplier() {
        return isActive() ? DAMAGE_MULTIPLIER : 1;
    }

    public float getVelocityMultiplier() {
        return isActive() ? VELOCITY_MULTIPLIER : 1;
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

        if (Math.random() >= JAMMING_CHANCE) {
            jammedTime = JAMMING_TIME;
            setActive(false);

            getUser().setBlocked(true);
            return false;
        }

        return true;
    }

}
