package net.michaeljackson23.mineademia.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundEvent.class)
public class SoundEventMixin {

    @Final
    @Shadow
    private boolean staticDistance;


    @ModifyReturnValue(method = "getDistanceToTravel", at = @At("RETURN"))
    public float getDistanceToTravel(float original, float volume) {
        if (!staticDistance)
            return volume * 16;

        return original;
    }

}
