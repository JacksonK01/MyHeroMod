package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ManchesterSmash extends BasicAbility {
    private boolean init = false;
    private int counter = 0;
    private boolean isDone = false;
    private boolean impact = false;

    public ManchesterSmash() {
        super(90, 50, 20, "Manchester Smash", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            AnimationProxy.sendAnimationToClients(player, "manchester_smash_shoot_style");
            player.addVelocity(0, 1.5, 0);
            player.velocityModified = true;
            init = true;
        }
        if(counter >= 11 && !isDone) {
            Vec3d lookVec = player.getRotationVector().multiply(2, 0, 2).add(0, -2, 0);
            player.setVelocity(lookVec);
            isDone = true;
            player.velocityModified = true;
        }
        if(!(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir()) && isDone) {
            impact = true;
            deactivate(player, quirk);
        }
        counter++;
    }

    @Override
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        super.deactivate(player, quirk);
        AnimationProxy.sendStopAnimation(player);
        counter = 0;
        init = false;
        isDone = false;
        if(impact) {
            player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                    player.getX(), player.getY(), player.getZ(),
                    1, 0, 0, 0, 0
            );
            player.getServerWorld().spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    player.getX(), player.getY() + 0.2, player.getZ(),
                    25, 1, 0, 1, 0.5
            );
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 0.2, player.getZ(),
                    25, 1, 0, 1, 1
            );
            AreaOfEffect.execute(player, 4, 2, player.getX(), player.getY(), player.getZ(), (entity) -> {
                entity.setVelocity(0, 1, 0);
                entity.velocityModified = true;

                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        1, 0, 0, 0, 0
                );
            });
        }
        impact = false;
    }
}
