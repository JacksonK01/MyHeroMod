package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IBlockReason;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EntityBlockReason implements IBlockReason {

    private final LivingEntity blocker;

    public EntityBlockReason(@NotNull LivingEntity blocker) {
        this.blocker = blocker;
    }

    @NotNull
    public LivingEntity getBlocker() {
        return blocker;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EntityBlockReason that = (EntityBlockReason) object;
        return Objects.equals(blocker, that.blocker);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(blocker);
    }

}
