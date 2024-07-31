package net.michaeljackson23.mineademia.abilitiestest.usage;

import net.michaeljackson23.mineademia.abilitiestest.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitiestest.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

/**
 * How an ability implementation in stages (Kinda like {@link net.michaeljackson23.mineademia.quirk.abilities.ofa.ManchesterSmash} works) could look like
 */
public final class TestAbility extends ActiveAbility implements ITickAbility, IStaminaAbility, ICooldownAbility {

    private boolean isRunning;
    private int phase;

    private int timer;

    private Cooldown cooldown;

    public TestAbility(@NotNull IAbilityUser user) {
        super(user, "Test Ability", "My Test Ability", AbilityCategory.OTHER);
    }

    @Override
    protected void init() {
        this.cooldown = new Cooldown(140);
    }

    @Override
    public void onTick() {
        if (!isRunning)
            return;

        firstPhase();
        secondPhase();
        thirdPhase();
    }

    @Override
    public void execute() {
        if (isReadyAndReset()) {
            phase = 0;
            isRunning = true;
        }
    }


    private void firstPhase() {
        if (phase != 0)
            return;

        timer++;
        if (timer >= 25) {
            timer = 0;
            phase++;
            System.out.println("PHASE 1 COMPLETE");
        }
    }

    private void secondPhase() {
        if (phase != 1)
            return;

        timer++;
        if (timer >= 10) {
            timer = 0;
            phase++;
            System.out.println("PHASE 2 COMPLETE");
        }}

    private void thirdPhase() {
        if (phase != 2)
            return;

        timer++;
        if (timer >= 5) {
            timer = 0;
            isRunning = false;
            System.out.println("PHASE 3 COMPLETE");
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown; // 7 seconds
    }

    @Override
    public int getStaminaCost() {
        return 50;
    }

}
