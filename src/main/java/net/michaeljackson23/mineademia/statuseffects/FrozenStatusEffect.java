package net.michaeljackson23.mineademia.statuseffects;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

public class FrozenStatusEffect extends StatusEffect {

    protected FrozenStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 100);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if(entity.getWorld() instanceof ServerWorld serverWorld)
            serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.BLUE_ICE.getDefaultState()), entity.getX(), entity.getY()+1, entity.getZ(), amplifier, 0, 0.5, 0, 0);
        var effect = entity.getStatusEffect(StatusEffectsRegister.EFFECT_FROZEN);
        if(effect.getDuration()==1){
            if(amplifier>0){
                entity.removeStatusEffect(StatusEffectsRegister.EFFECT_FROZEN);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegister.EFFECT_FROZEN, 40, amplifier-1));
            }
            entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.5f, 0.4f / (entity.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        }

    }
    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        super.onRemoved(attributeContainer);

    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
    }
}
