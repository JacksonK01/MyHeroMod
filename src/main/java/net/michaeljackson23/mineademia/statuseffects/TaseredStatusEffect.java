package net.michaeljackson23.mineademia.statuseffects;

import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.PlaceClientParticleInWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class TaseredStatusEffect extends StatusEffect {

    protected TaseredStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xFFFF00);
    }

    @Override
    public void applyUpdateEffect(LivingEntity self, int amplifier) {
        super.applyUpdateEffect(self, amplifier);

        if(self.getWorld() instanceof ServerWorld world) {
            world.spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                    self.getX(), self.getY() + 1, self.getZ(), amplifier,
                    1, 1, 1, 0);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
