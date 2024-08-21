package net.michaeljackson23.mineademia.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityDecoders;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @ModifyVariable(method = "renderEntity", at = @At("HEAD"), ordinal = 0)
    public float renderEntity(float tickDelta, Entity entity) {
        // AbilityUserPacketS2C user = ClientCache.self;
        //if (user == null)
        //    return tickDelta;

        // HashMap<Class<?>, IReadonlyTypesafeMap> abilities = user.abilities();
        if (ClientCache.getAbilities(TimeStopAbility.class).stream().anyMatch((a) -> a.get(AbilityDecoders.ACTIVATION_ABILITY_IS_ACTIVE))) {
            // IReadonlyTypesafeMap typesafeMap = abilities.get("Time Stop");
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null)
                return tickDelta;

             if (player.getPos().squaredDistanceTo(entity.getPos()) <= 50 * 50)
                return 0;
        }

        return tickDelta;
    }

}
