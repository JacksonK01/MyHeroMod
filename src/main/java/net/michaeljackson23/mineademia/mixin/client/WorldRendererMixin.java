package net.michaeljackson23.mineademia.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityDecoders;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityKeys;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @ModifyVariable(method = "renderEntity", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public float renderEntityModifyDelta(float tickDelta, Entity entity) {
        if (ClientCache.getAbilities(TimeStopAbility.class).stream().anyMatch((a) -> a.get(AbilityKeys.IS_ACTIVE))) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null || ClientCache.getSelfAbilities(TimeStopAbility.class).isEmpty())
                return tickDelta;

             if (player.getPos().squaredDistanceTo(entity.getPos()) <= TimeStopAbility.MAX_RANGE_SQUARED)
                return 0;
        }

        return tickDelta;
    }

    @WrapWithCondition(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public boolean renderEntityCancel(EntityRenderDispatcher instance, Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (ClientCache.getAbilities(TimeStopAbility.class).stream().anyMatch((a) -> a.get(AbilityKeys.IS_ACTIVE))) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null)
                return true;

            if (player.getPos().squaredDistanceTo(entity.getPos()) <= TimeStopAbility.MAX_RANGE_SQUARED)
                return !ClientCache.getSelfAbilities(TimeStopAbility.class).isEmpty();
        }

        return true;
    }

}
