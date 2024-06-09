package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.callbacks.BeforeEntityDamageCallback;
import net.michaeljackson23.mineademia.util.MutableObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private MutableObject<Float> damageWrapper;

    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        damageWrapper = new MutableObject<>(amount);
        ActionResult result = BeforeEntityDamageCallback.EVENT.invoker().onDamage(entity, source, damageWrapper);
        if (result == ActionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageAmount(float amount) {
        if (damageWrapper != null) {
            amount = damageWrapper.getData();
            damageWrapper = null;
        }
        return amount;
    }
}
