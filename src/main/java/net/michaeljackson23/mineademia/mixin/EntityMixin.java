package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.NoClipAbility;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
//    @Inject( method = "move", at = @At("HEAD"))
//    private void move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
//        Entity entity = getSelf();
//
//        if (!(entity instanceof LivingEntity livingEntity))
//            return;
//
//        IAbilityUser user = AbilityManager.getUser(livingEntity);
//        if (user == null)
//            return;
//
//        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
//        if (noClipAbility == null)
//            return;
//
//        entity.noClip |= noClipAbility.isClipping();
//        // entity.setOnGround(entity.noClip);
//    }
//
//    @Inject( method = "isInsideWall", at = @At("HEAD") )
//    private void isInsideWall(CallbackInfoReturnable<Boolean> cir) {
//        Entity entity = getSelf();
//
//        if (!(entity instanceof LivingEntity livingEntity))
//            return;
//
//        IAbilityUser user = AbilityManager.getUser(livingEntity);
//        if (user == null)
//            return;
//
//        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
//        if (noClipAbility == null)
//            return;
//
//        entity.noClip |= noClipAbility.isClipping();
//    }

    @Unique
    private Entity getSelf() {
        return (Entity) (Object) this;
    }

}
