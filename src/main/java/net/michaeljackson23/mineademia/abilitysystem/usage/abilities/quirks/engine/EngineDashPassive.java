package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.engine;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class EngineDashPassive extends ActiveAbility implements ICooldownAbility, ITickAbility {

    private static final int COOLDOWN_TIME = 0;
    private static final int MAX_DASH_TIMER = 60;
    private static final int MIN_FOR_ENGINE_DASH = 40;
    private static final int MAX_EFFECT_TIMER = 5;
    private static final int MAX_ANIMATION_COUNTER = 25;
    private static final int MAX_IN_AIR = 4;
    private static final double VELOCITY_MULTIPLIER = 0.75;

    private int dashTimer = 0;
    private int effectTimer = MAX_EFFECT_TIMER;
    private int animationCounter = 0;
    private int inAirTimer = 0;
    private boolean cancelAnimation = false;
    private boolean hasSentAnimation = false;

    private final Cooldown cooldown;

    public EngineDashPassive(@NotNull IAbilityUser user) {
        super(user, "Engine Dash", "After sprinting for a second, your engines will activate and you'll start dashing.", AbilityCategory.MOBILITY);
        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public void onStartTick() {
        engineDash();
    }

    @Override
    public void execute(boolean isKeyDown) {
        // No execution logic needed for this ability
    }

    private void engineDash() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (entity.isSprinting()) {
            if (dashTimer < MAX_DASH_TIMER) {
                dashTimer++;
            }
        } else {
            if (dashTimer > 0) {
                dashTimer--;
            }
            hasSentAnimation = false;
        }

        if (dashTimer >= MIN_FOR_ENGINE_DASH) {
            applyEngineDashEffects(entity, world);
            sendResetPacket();
        }
    }

    private void applyEngineDashEffects(LivingEntity entity, ServerWorld world) {
        Vec3d velocity = getPlayerVector(entity).multiply(VELOCITY_MULTIPLIER);
        entity.setVelocity(velocity.x, entity.getVelocity().y, velocity.z);
        entity.velocityModified = true;

        spawnParticles(entity, world);
        handleAnimation(entity);
        handleAnimationCancelInAir(entity, world);

        if (entity.age % 10 == 0) {
            spawnExplosionParticles(entity, world);
            playSound(entity, world);
        }
    }

    private Vec3d getPlayerVector(LivingEntity entity) {
        double yawRad = Math.toRadians(entity.getYaw());
        return new Vec3d(-Math.sin(yawRad), 0, Math.cos(yawRad)).normalize();
    }

    private void spawnParticles(LivingEntity entity, ServerWorld world) {
        world.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0.2, 0, 0.01);
        world.spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0.2, 0, 0.01);
    }

    private void spawnExplosionParticles(LivingEntity entity, ServerWorld world) {
        world.spawnParticles(ParticleTypes.EXPLOSION, entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0.2, 0, 1);
    }

    private void playSound(LivingEntity entity, ServerWorld world) {
        world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1f, 2f);
    }

    private void handleAnimation(LivingEntity entity) {
        if (animationCounter > MAX_ANIMATION_COUNTER && !cancelAnimation) {
            if (!hasSentAnimation) {
                AnimationProxy.sendAnimationToClients(entity, "engine_dash");
                hasSentAnimation = true;
            }
            animationCounter = 0;
        } else {
            animationCounter++;
        }
    }

    private void handleAnimationCancelInAir(LivingEntity entity, ServerWorld world) {
        if (world.getBlockState(entity.getBlockPos().down()).isAir()) {
            if (inAirTimer >= MAX_IN_AIR) {
                AnimationProxy.sendStopAnimation(entity);
                cancelAnimation = true;
                hasSentAnimation = false;
            }
            inAirTimer++;
        } else {
            resetInAirTimer();
        }
    }

    private void resetInAirTimer() {
        inAirTimer = 0;
        cancelAnimation = false;
    }

    private void sendResetPacket() {
        if(dashTimer == MIN_FOR_ENGINE_DASH) {
            AnimationProxy.sendStopAnimation(getEntity());
        }
    }
}
