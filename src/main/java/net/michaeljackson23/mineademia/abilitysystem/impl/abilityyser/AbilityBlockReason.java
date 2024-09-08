package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IBlockReason;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record AbilityBlockReason(IAbility blocker) implements IBlockReason {

    public AbilityBlockReason(@NotNull IAbility blocker) {
        this.blocker = blocker;
    }

    @Override
    @NotNull
    public IAbility blocker() {
        return blocker;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AbilityBlockReason that = (AbilityBlockReason) object;
        return Objects.equals(blocker, that.blocker);
    }


}
