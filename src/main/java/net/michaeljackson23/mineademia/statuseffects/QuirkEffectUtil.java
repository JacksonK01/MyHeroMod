package net.michaeljackson23.mineademia.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public class QuirkEffectUtil {
    static public void applyFrozen(LivingEntity entity, int duration){
        if(entity.hasStatusEffect(StatusEffectsRegister.EFFECT_FROZEN)){
            StatusEffectInstance effectInstance = entity.getStatusEffect(StatusEffectsRegister.EFFECT_FROZEN);
            int a;
            if(effectInstance.getAmplifier()<5)
                a = effectInstance.getAmplifier()+1;
            else
                a = effectInstance.getAmplifier();
            entity.removeStatusEffect(StatusEffectsRegister.EFFECT_FROZEN);
            entity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegister.EFFECT_FROZEN, duration, a));
        }else{
            entity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegister.EFFECT_FROZEN, duration, 0));
        }
    }
}
