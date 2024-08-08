package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.animations.AnimationDataHolder;
import net.michaeljackson23.mineademia.callbacks.BeforeEntityDamageCallback;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.michaeljackson23.mineademia.util.MutableObject;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityMixinAccessor {

    @Shadow public abstract void animateDamage(float yaw);

    @Unique
    private MutableObject<Float> damageWrapper;

    @Unique
    public AnimationState activeAnimationState = new AnimationState();

    @Unique
    public AnimationDataHolder activeAnimation = new AnimationDataHolder(Animation.Builder.create(0f).build());

    @Unique
    private boolean isEndTimerStored = false;
    @Unique
    private float animationDuration = 0f;

    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = getSelf();
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

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        if(getSelf().getWorld().isClient)
            updateAnimations();
    }

    @Unique
    private void updateAnimations() {
        if(activeAnimationState == null || activeAnimation == null)
            return;

        if(activeAnimationState.isRunning()) {
            handleAnimationEnd();
        } else {
            resetAnimationState();
        }
    }

    @Unique
    private void handleAnimationEnd() {
        LivingEntity self = getSelf();
        Animation animation = getAnimationData().getAnimation();

        if (!isEndTimerStored && !animation.looping()) {
            animationDuration = self.age + (animation.lengthInSeconds() * 20);
            isEndTimerStored = true;
        }

        getSelf().sendMessage(Text.literal("Duration: " + self.age + "/" + animationDuration));

        if (isEndTimerStored && !animation.looping() && self.age >= animationDuration) {
            resetAnimationState();
            activeAnimationState.stop();
        }
    }

    @Unique
    private void resetAnimationState() {
        animationDuration = 0f;
        isEndTimerStored = false;
    }

    @Override
    public void setAnimationData(AnimationDataHolder animation) {
        resetAnimationState();
        this.activeAnimation = animation;
    }

    @Override
    public void setAnimationState(AnimationState state) {
        resetAnimationState();
        this.activeAnimationState = state;
    }

    @Override
    public AnimationDataHolder getAnimationData() {
        return this.activeAnimation;
    }

    @Override
    public AnimationState getAnimationState() {
        return this.activeAnimationState;
    }

    @Unique
    private LivingEntity getSelf() {
        return (LivingEntity) (Object) this;
    }
}
