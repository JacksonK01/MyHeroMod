package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class Explode extends BasicAbility {
    private boolean init = false;
    private boolean isAir = false;
    private boolean init2 = false;

    public Explode() {
        super(11, 75, 5, "Explode", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            init(player);
        }
        if(isAir && !init2) {
            spawnParticlesUnderHands(player, ParticleTypes.EXPLOSION);
            player.getWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), 2.5f, false, World.ExplosionSourceType.TNT);
            player.setVelocity(player.getRotationVector().multiply(3.5).add(0, 0.5, 0));
            player.velocityModified = true;
            AnimationProxy.sendAnimationToClients(player, "explode_flip");
        } else if(!init2){
            spawnParticlesUnderHands(player, ParticleTypes.EXPLOSION);
            spawnParticlesUnderHands(player, ModParticles.QUIRK_EXPLOSION_DETONATION);
            player.getWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), 1.5f, true, World.ExplosionSourceType.TNT);
            player.setVelocity(player.getVelocity().x, 1, player.getVelocity().z);
            player.velocityModified = true;
            deActivate(player, quirk);
            return;
        }
        if(isAir) {
            spawnParticlesUnderHands(player, ParticleTypes.SMOKE);
        }
        init2 = true;
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        if(isAir) {
            AnimationProxy.sendStopAnimation(player);
        }
        init = false;
        init2 = false;
        isAir = false;
    }

    private void init(ServerPlayerEntity player) {
        isAir = player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir();
        player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 2f);
        init = true;
    }

    private void spawnParticlesUnderHands(ServerPlayerEntity player, DefaultParticleType type) {
        double pitch = ((player.getPitch() + 90) * Math.PI) / 180;
        double yaw = ((player.getYaw() + 90) * Math.PI)/ 180;
        double x = Math.cos(yaw) ;
        double y = Math.sin(pitch);
        double z = Math.sin(yaw);

        player.getServerWorld().spawnParticles(type,
                player.getX() + z*.5, player.getY() + 0.7, player.getZ() - x*.5,
                1, 0, 0, 0, 0);

        player.getServerWorld().spawnParticles(type,
                player.getX() - z*.5, player.getY() + 0.7, player.getZ() + x*.5,
                1, 0, 0, 0, 0);
    }
}
