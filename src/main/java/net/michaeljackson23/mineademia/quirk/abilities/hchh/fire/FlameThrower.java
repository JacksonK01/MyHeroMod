package net.michaeljackson23.mineademia.quirk.abilities.hchh.fire;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class FlameThrower extends AbilityBase {
    private boolean didPlaySound = false;

    public FlameThrower() {
        super(100, 10, 110, false, "Flame Thrower", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!didPlaySound) {
            player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.MHA_FIRE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            didPlaySound = true;
        }
        player.swingHand(Hand.OFF_HAND, true);
        Vec3d playerVec = player.getRotationVec(1.0f);
        int distance = 4;
        int stepSize = 4;
        for(int d = stepSize; d < distance * stepSize; d += stepSize) {
            double x = player.getX() + playerVec.getX() * d;
            double z = player.getZ() + playerVec.getZ() * d;
            double y = player.getY() + 1 + playerVec.getY() * d;

            player.getServerWorld().spawnParticles(ParticleTypes.FLAME, x, y, z,
                    5, 2, 2, 2, 0.001f);

            AreaOfEffect.execute(player, 4, 2, x, y, z, (entityToAffect) -> {
                QuirkDamage.doDamage(player, entityToAffect, 6);
                entityToAffect.setOnFireFor(4);
                player.getServerWorld().spawnParticles(ParticleTypes.FLAME,
                        entityToAffect.getX(), entityToAffect.getY(), entityToAffect.getZ(),
                        15, entityToAffect.getWidth(), entityToAffect.getHeight(), entityToAffect.getWidth(),
                        0);
                entityToAffect.setVelocity(playerVec.multiply(0.5));
                entityToAffect.velocityModified = true;
            });
        }
    }

    @Override
    protected void deactivate() {
        super.deactivate();
        didPlaySound = false;
    }
}
