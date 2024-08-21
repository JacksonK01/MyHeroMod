package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.ScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public class HairAmmoAbility extends ScheduledPassiveAbility {

    public static final String DESCRIPTION = "Your eyesight is unaffected by neither darkness nor weather conditions, such as heavy rainfall.";

    public static final float MAX_AMMO = 100f;

    public static final int SCHEDULE_FREQUENCY = 1;
    public static final float AMMO_PER_INCREASE = 1;// 1 / 40f;


    private float ammoAmount;

    public HairAmmoAbility(@NotNull IAbilityUser user) {
        super(user, "Hair Ammo", DESCRIPTION, SCHEDULE_FREQUENCY);

        this.ammoAmount = 0;
    }

    @Override
    public void execute(boolean isKeyDown) {
        this.ammoAmount = Math.min(MAX_AMMO, ammoAmount + AMMO_PER_INCREASE);
    }

    public float getAmmoAmount() {
        return ammoAmount;
    }

    public boolean tryRemoveAmmo(float ammoAmount) {
        if (this.ammoAmount >= ammoAmount) {
            this.ammoAmount -= ammoAmount;
            return true;
        }

        return false;
    }

}
