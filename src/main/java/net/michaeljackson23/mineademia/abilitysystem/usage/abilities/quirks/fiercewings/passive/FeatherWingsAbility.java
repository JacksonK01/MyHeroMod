package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.fiercewings.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.ScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.Mathf;
import org.jetbrains.annotations.NotNull;

public class FeatherWingsAbility extends ScheduledPassiveAbility {

    public static final String DESCRIPTION = "";

    public static final int MAX_COUNT = 200;

    public static final int REGEN_RATE = 1;
    public static final int REGEN_AMOUNT = 0;
    public static final int REGEN_DELAY = 0;


    private int featherCount;
    private int regenTimer;

    public FeatherWingsAbility(@NotNull IAbilityUser user) {
        super(user, "Feather Wings", DESCRIPTION, REGEN_RATE);
        this.featherCount = 0;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (regenTimer > 0) {
            regenTimer = Math.max(0, regenTimer - REGEN_RATE);
            return;
        }

        setFeatherCount(getFeatherCount() + REGEN_AMOUNT);
    }

    public int getFeatherCount() {
        return featherCount;
    }

    public void setFeatherCount(int featherCount, boolean startTimer) {
        if (startTimer && featherCount < this.featherCount)
            this.regenTimer = REGEN_DELAY;

        this.featherCount = Mathf.clamp(0, MAX_COUNT, featherCount);
    }
    public void setFeatherCount(int featherCount) {
        setFeatherCount(featherCount, false);
    }

    public float getPartialValue() {
        return (float) featherCount / MAX_COUNT;
    }

}
