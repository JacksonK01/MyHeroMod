package net.michaeljackson23.mineademia.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin {

    @WrapWithCondition(method = "tickEntity", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    public boolean tickEntity(Consumer<?> tickConsumer, Object entity) {
        Entity castEntity = (Entity) entity;
        boolean result = IAbility.getAbilities(TimeStopAbility.class, false).stream().noneMatch((a) -> a.shouldTimeStop(castEntity));
        if (!result) {
            if (castEntity instanceof LivingEntity livingEntity && livingEntity.age % 20 == 0) {
                livingEntity.getDamageTracker().update();
            }
        }

        return result;
    }

}
