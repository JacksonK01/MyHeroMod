package net.michaeljackson23.mineademia.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;

public class CowlingStatusEffect extends StatusEffect {
    protected CowlingStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 5797459);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
//        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 2, amplifier, true, false, false));
//        if(amplifier >= 5) {
//            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2, 0, true, false, false));
//        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
