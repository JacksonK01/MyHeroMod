package net.michaeljackson23.mineademia.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.networking.NetworkKeys;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private ClientWorld world;


    @ModifyVariable(method = "renderEntity", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public float renderEntityModifyDelta(float tickDelta, Entity entity) {
        HashSet<IReadonlyTypesafeMap> abilities = ClientCache.getAbilities(TimeStopAbility.class, false);
        boolean isTimeStopped = abilities.stream().anyMatch((a) -> TimeStopAbility.isTimeStoppedClient(world, a));

        if (isTimeStopped) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null)
                return tickDelta;

             if (abilities.stream().anyMatch((a) -> TimeStopAbility.isInsideTimeStopClient(world, a, player)))
                return 0;
        }

        return tickDelta;
    }

    @WrapWithCondition(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public boolean renderEntityCancel(EntityRenderDispatcher instance, Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
//        if (ClientCache.getAbilities(TimeStopAbility.class).stream().anyMatch((a) -> a.get(NetworkKeys.IS_ACTIVE))) {
//            ClientPlayerEntity player = MinecraftClient.getInstance().player;
//            if (player == null)
//                return true;
//
//            if (player.getPos().squaredDistanceTo(entity.getPos()) <= TimeStopAbility.MAX_RANGE_SQUARED)
//                return !ClientCache.getSelfAbilities(TimeStopAbility.class).isEmpty();
//        }

        return true;
    }

    @Unique
    private boolean shouldStopTime(@NotNull Entity entity) {
        return ClientCache.getAbilities(TimeStopAbility.class, false).stream().anyMatch((a) -> TimeStopAbility.shouldStopTimeClient(world, a, entity));
    }

}
