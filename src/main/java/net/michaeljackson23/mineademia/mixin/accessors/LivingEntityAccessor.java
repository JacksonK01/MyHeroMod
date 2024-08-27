package net.michaeljackson23.mineademia.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("jumping")
    boolean getJumping();

    @Accessor("jumpingCooldown")
    int getJumpingCooldown();

    @Accessor("jumpingCooldown")
    void setJumpingCooldown(int jumpingCooldown);

    @Invoker("jump")
    void invokeJump();

    @Invoker("setLivingFlag")
    void invokeSetLivingFlat(int mask, boolean value);

}
