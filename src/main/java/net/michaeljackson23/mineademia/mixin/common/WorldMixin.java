package net.michaeljackson23.mineademia.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin {

    @WrapWithCondition(method = "tickEntity", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    public boolean tickEntity(Consumer<Entity> tickConsumer, Object entity) {
        Entity castEntity = (Entity) entity;
        return getSelf().isClient ? !shouldStopTimeClient(castEntity) : !shouldStopTimeServer(castEntity);
    }

    @ModifyReturnValue(method = "canPlayerModifyAt", at = @At("RETURN"))
    public boolean canPlayerModifyAt(boolean original, PlayerEntity player, BlockPos pos) {
        boolean shouldCancel = getSelf().isClient ? shouldStopTimeClient(player) : shouldStopTimeServer(player);
        return !shouldCancel && original;
    }

    @Unique
    private boolean shouldStopTimeServer(@NotNull Entity entity) {
        boolean shouldStopTime = IAbility.getAbilities(TimeStopAbility.class, false).stream().anyMatch((a) -> a.shouldTimeStop(entity));
        if (shouldStopTime) {
            if (entity instanceof LivingEntity livingEntity && livingEntity.age % 20 == 0) {
                livingEntity.getDamageTracker().update();
            }

            return true;
        }

        return false;
    }
    @Unique
    private boolean shouldStopTimeClient(@NotNull Entity entity) {
        World world = getSelf();
        return ClientCache.getAbilities(TimeStopAbility.class, false).stream().anyMatch((a) -> TimeStopAbility.shouldStopTimeClient(world, a, entity));
    }


    @Unique
    private World getSelf() {
        return (World) (Object) this;
    }

}
