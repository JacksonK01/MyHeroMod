package net.michaeljackson23.mineademia.abilitysystem.usage;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

/**
 * How an ability implementation in stages (Kinda like {@link net.michaeljackson23.mineademia.quirk.abilities.ofa.ManchesterSmash} works) could look like
 */
public final class TestAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {

    private boolean isRunning;
    private int phase;

    private int timer;

    private final Cooldown cooldown;

    public TestAbility(@NotNull IAbilityUser user) {
        super(user, "Test Ability", "My Test Ability");
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
    public void execute(boolean isKeyDown) {
        if (isCooldownReadyAndReset()) {
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

}
