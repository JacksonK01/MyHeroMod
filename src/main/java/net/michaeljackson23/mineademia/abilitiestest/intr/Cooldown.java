package net.michaeljackson23.mineademia.abilitiestest.intr;

import org.jetbrains.annotations.Nullable;

/**
 * Basic cooldown class
 */
public final class Cooldown {

    private int cooldownTicks;
    private int ticksRemaining;

    private Runnable onReady;

    public Cooldown(int cooldownTicks, @Nullable Runnable onReady) {
        this.cooldownTicks = cooldownTicks;
        this.ticksRemaining = cooldownTicks;

        this.onReady = onReady;
    }
    public Cooldown(int cooldownTicks) {
        this(cooldownTicks, null);
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }
    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = Math.max(0, cooldownTicks);
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }
    public void setTicksRemaining(int ticksRemaining) {
        this.ticksRemaining = Math.max(0, ticksRemaining);

        if (this.ticksRemaining == 0 && onReady != null)
            onReady.run();
    }

    public void setOnReady(@Nullable Runnable onReady) {
        this.onReady = onReady;
    }

    public boolean isReady() {
        return getTicksRemaining() <= 0;
    }

    public void reset() {
        setTicksRemaining(getCooldownTicks());
    }

    public void onTick() {
        setTicksRemaining(getTicksRemaining() - 1);
    }

    public boolean isReadyAndReset() {
        if (isReady()) {
            reset();
            return true;
        }

        return false;
    }

}
