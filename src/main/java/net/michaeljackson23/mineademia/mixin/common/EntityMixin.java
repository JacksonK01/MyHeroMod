package net.michaeljackson23.mineademia.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.networking.NetworkKeys;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.EntityRenderAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.NoClipAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.util.GlowingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "shouldRender(D)Z", at = @At("RETURN"))
    private boolean shouldRender(boolean original, double distance) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null)
            return original;

        HashSet<IReadonlyTypesafeMap> abilities = ClientCache.getSelfAbilities(EntityRenderAbility.class, false);
        for (IReadonlyTypesafeMap ability : abilities) {
            if (!ability.getOrDefault(NetworkKeys.IS_ACTIVE, false))
                continue;

            float range = ability.getOrDefault(NetworkKeys.RANGE, -1f);
            boolean shouldRender = range < 0 ? original : distance < range;
            if (shouldRender)
                return true;
        }

        return original;
    }

    @Inject( method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true )
    private void pushOutOfBlocks(double x, double y, double z, CallbackInfo ci) {
        Entity entity = getSelf();

        if (!(entity instanceof LivingEntity livingEntity))
            return;

        IAbilityUser user = AbilityManager.getUser(livingEntity);
        if (user == null)
            return;

        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
        if (noClipAbility == null || !noClipAbility.isNoClipping())
            return;

        ci.cancel();
    }

    @Environment(EnvType.CLIENT)
    @ModifyReturnValue(method = "getTeamColorValue", at = @At("RETURN"))
    private int getTeamColorValue(int original) {
        Entity self = getSelf();

        if (!GlowingHelper.hasColor(self))
            return original;

        return GlowingHelper.getColor(self);
    }


    @Unique
    private Entity getSelf() {
        return (Entity) (Object) this;
    }

}
