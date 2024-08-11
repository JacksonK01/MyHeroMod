package net.michaeljackson23.mineademia.abilitysystem.impl.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IPhaseAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class PhaseAbility extends ActiveAbility implements IPhaseAbility {

    private int phase;
    private int ticks;

    private final HashMap<Integer, Runnable> phaseMethods;
    private final HashMap<Integer, Runnable> startPhaseMethods;

    public PhaseAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, @NotNull AbilityCategory @NotNull... categories) {
        super(user, name, description, defaultIdentifier, categories);

        phaseMethods = new HashMap<>();
        startPhaseMethods = new HashMap<>();

        resetPhase();
    }
    public PhaseAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory... categories) {
        this(user, name, description, null, categories);
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public void setPhase(int phase) {
        this.phase = Math.max(-1, phase);

        Runnable startPhaseMethod = getStartPhaseMethod(this.phase);
        if (startPhaseMethod != null)
            startPhaseMethod.run();
    }

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public void setTicks(int ticks) {
        this.ticks = Math.max(-1, ticks);
    }

    @Override
    public @Nullable Runnable getPhaseMethod(int phase) {
        return phaseMethods.get(phase);
    }

    @Override
    public void setPhaseMethod(int phase, @NotNull Runnable runnable) {
        phaseMethods.put(phase, runnable);
    }

    @Override
    public @Nullable Runnable getStartPhaseMethod(int phase) {
        return startPhaseMethods.get(phase);
    }

    @Override
    public void setStartPhaseMethod(int phase, @NotNull Runnable runnable) {
        startPhaseMethods.put(phase, runnable);
    }

}
