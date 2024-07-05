package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.callbacks.OnPlayerAttackEntity;
import net.michaeljackson23.mineademia.util.MutableObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private MutableObject<Float> damageWrapperOnAttack;

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getVelocity()Lnet/minecraft/util/math/Vec3d;"), locals = LocalCapture.CAPTURE_FAILHARD,cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci, float damage) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (target instanceof LivingEntity livingEntity && player instanceof ServerPlayerEntity serverPlayer) {
            damageWrapperOnAttack = new MutableObject<>(damage);
            ActionResult result = OnPlayerAttackEntity.EVENT.invoker().onAttack(serverPlayer, livingEntity, damageWrapperOnAttack);
            if (result == ActionResult.FAIL) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getVelocity()Lnet/minecraft/util/math/Vec3d;"), ordinal = 0)
    private float modifyDamageAmount(float damage) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(player instanceof ServerPlayerEntity serverPlayer) {
            if (damageWrapperOnAttack != null) {
                damage = damageWrapperOnAttack.getData();
                damageWrapperOnAttack = null;
            }
        }
        return damage;
    }
}
