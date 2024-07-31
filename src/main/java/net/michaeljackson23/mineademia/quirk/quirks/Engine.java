package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.engine.PlummetJump;
import net.michaeljackson23.mineademia.quirk.abilities.engine.SlideAndKicks;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class Engine extends Quirk {
    private int dashTimer = 0;
    private final int MAX_DASH_TIMER = 60;
    private final int MIN_FOR_ENGINE_DASH = 40;

    private final int MAX_EFFECT_TIMER = 5;
    private int effectTimer = MAX_EFFECT_TIMER;

    private int animationCounter = 0;
    private int MAX_ANIMATION_COUNTER = 25;

    private int inAirTimer = 0;
    private int MAX_IN_AIR = 4;
    private boolean cancelAnimation = false;

    public Engine() {
        super("Engine", new PlummetJump(), new Empty(), new SlideAndKicks(), new Empty(), new Empty());
        setModelsForQuirk("EnginesAndEngineFire");
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        super.tick(player);
        engineDash(player);
    }

    private void engineDash(ServerPlayerEntity player) {
        if(player.isSprinting()) {
            if(dashTimer < MAX_DASH_TIMER) {
                dashTimer++;
            }
        } else {
            if(dashTimer > 0) {
                dashTimer--;
            }
        }

        if(dashTimer >= MIN_FOR_ENGINE_DASH) {
            Vec3d velocity = getPlayerVector(player).multiply(0.75);
            player.setVelocity(velocity.x, player.getVelocity().y, velocity.z);
            player.velocityModified = true;
            spawnParticles(player);
            handleAnimation(player);
            handleAnimationCancelInAir(player);
            animationCounter++;
            if(player.age % 10 == 0) {
                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                        player.getX(), player.getY(), player.getZ(),
                        1, 0, 0.2, 0, 1);
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1f, 2f);
            }
        }
    }

    private Vec3d getPlayerVector(ServerPlayerEntity player) {
        double yawRad = Math.toRadians(player.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize();
    }

    private void spawnParticles(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        world.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                player.getX(), player.getY(), player.getZ(),
                1, 0, 0.2, 0, 0.01);
        world.spawnParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                1, 0, 0.2, 0, 0.01);
    }

    private void handleAnimation(ServerPlayerEntity player) {
        if(animationCounter > MAX_ANIMATION_COUNTER && !cancelAnimation) {
            AnimationProxy.sendAnimationToClients(player, "engine_dash");
            animationCounter = 0;
        } else {
            animationCounter++;
        }
    }

    private void handleAnimationCancelInAir(ServerPlayerEntity player) {
        if(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir()) {
            if(inAirTimer >= MAX_IN_AIR) {
                AnimationProxy.sendStopAnimation(player);
                cancelAnimation = true;
            }
            inAirTimer++;
        } else {
            inAirTimer = 0;
            cancelAnimation = false;
        }
    }
}
