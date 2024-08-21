package net.michaeljackson23.mineademia.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.NoClipAbility;
import net.michaeljackson23.mineademia.util.GlowingHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject( method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true )
    private void pushOutOfBlocks(double x, double y, double z, CallbackInfo ci) {
        Entity entity = getSelf();

        if (!(entity instanceof LivingEntity livingEntity))
            return;

        IAbilityUser user = AbilityManager.getUser(livingEntity);
        if (user == null)
            return;

        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
        if (noClipAbility == null || !noClipAbility.isClipping())
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
