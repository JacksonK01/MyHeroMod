package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPhaseAbility extends IActiveAbility, ITickAbility {

    int getPhase();
    void setPhase(int phase);

    @Nullable
    Runnable getPhaseMethod(int phase);
    void setPhaseMethod(int phase, @NotNull Runnable runnable);

    @Nullable
    Runnable getStartPhaseMethod(int phase);
    void setStartPhaseMethod(int phase, @NotNull Runnable runnable);

    default void resetPhase() {
        setPhase(-1);
    }

    default void nextPhase() {
        setPhase(getPhase() + 1);
    }

    @Nullable
    default Runnable getCurrentPhase() {
        return getPhaseMethod(getPhase());
    }

    default void setPhaseMethods(int startPhase, @NotNull Runnable @NotNull ... runnables) {
        startPhase = Math.max(0, startPhase);

        for (int i = 0; i < runnables.length; i++)
            setPhaseMethod(startPhase + i, runnables[i]);
    }
    default void setStartPhaseMethods(int startPhase, @NotNull Runnable @NotNull ... runnables) {
        startPhase = Math.max(0, startPhase);

        for (int i = 0; i < runnables.length; i++)
            setStartPhaseMethod(startPhase + i, runnables[i]);
    }

    @Override
    default void execute(boolean isKeyDown) {
        setPhase(0);
    }

    @Override
    default void onStartTick() {
        Runnable phaseMethod = getCurrentPhase();
        if (phaseMethod != null)
            phaseMethod.run();
    }

    @Override
    default void cancel() {
        resetPhase();
    }

}
