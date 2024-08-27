package net.michaeljackson23.mineademia.mixin.common;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @WrapWithCondition(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tick()V"))
    private boolean playerTick(PlayerEntity instance) {
        ServerPlayerEntity player = (ServerPlayerEntity) instance;

        boolean shouldStopTime = IAbility.getAbilities(TimeStopAbility.class, false).stream().anyMatch((a) -> a.shouldTimeStop(player));
        if (shouldStopTime) {
            if (player.age % 20 == 0)
                player.getDamageTracker().update();

            return false;
        }

        return true;
    }

}
