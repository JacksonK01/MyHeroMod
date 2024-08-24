package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.michaeljackson23.mineademia.util.Quad;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DetroitSmashAbility extends HoldAbility implements ICooldownAbility {

    private static final int COOLDOWN = 45;
    private static final int MAX_RADIUS_SIZE = 3;
    private static final int MAX_AMOUNT_OF_RADIUS = 4;
    private static final float MIN_SHRINK_SPEED = 0.1f;
    private static final float MAX_SHRINK_SPEED = 0.45f;
    private static final float MAX_SIZE_MULTIPLIER = 0.55f;
    private static final float MIN_SIZE_MULTIPLIER = 0.2f;
    private static final float MAX_CIRCLE_LIFE = 10;
    private static final int MAX_CHARGE = 45;

    private static final int STAGE_PRE_PUNCH = 1;
    private static final int STAGE_SHOCK_WAVES = 2;

    private final Cooldown cooldown;
    private int ticksHeldFor = 0;

    private final LinkedList<Quad<Vec3d, Integer, Float, Float>> radiusList;

    private float yawCache = 0f;
    private Vec3d posCache;
    private Vec3d vectorCache;
    private int stage = 0;
    private int inActiveTicks = 0;
    private boolean hasDisplayedShockwave = false;

    public DetroitSmashAbility(@NotNull IAbilityUser user) {
        super(user, "Detroit Smash", "Charge your fist and release for a giant punch", AbilityCategory.ATTACK, AbilityCategory.ANIMATION);
        this.cooldown = new Cooldown(COOLDOWN);

        this.radiusList = new LinkedList<>();
    }

    @Override
    public boolean executeStart() {
        LivingEntity entity = getEntity();

        reset();

        if(cooldown.isReadyAndReset()) {
            AnimationProxy.sendAnimationToClients(entity, "detroit_smash_charge");
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.OFA_CHARGE, SoundCategory.PLAYERS, 2f, 0.75f);
            return true;
        }
        return false;
    }

    @Override
    public void executeEnd() {
        ticksHeldFor = getTicks() > MAX_CHARGE ? MAX_CHARGE : getTicks();
        stage = STAGE_PRE_PUNCH;
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        entity.setVelocity(0, entity.getVelocity().y, 0);
        entity.velocityModified = true;

        int currentTick = getTicks();

        if (radiusList.size() < MAX_AMOUNT_OF_RADIUS) {
            int elementsToAdd = MAX_AMOUNT_OF_RADIUS - radiusList.size();

            for (int i = 0; i < elementsToAdd; i++) {
                Vec3d normal = new Vec3d(Mathf.random(-1, 1), Mathf.random(-1, 1), Mathf.random(-1, 1));

                float speed = Mathf.random(MIN_SHRINK_SPEED, MAX_SHRINK_SPEED);
                float sizeMultiplier = Mathf.random(MIN_SIZE_MULTIPLIER, MAX_SIZE_MULTIPLIER);

                radiusList.add(new Quad<>(normal, currentTick, speed, sizeMultiplier));
            }
        }

        //Uses iterator, so we can remove the element once the timer is reached
        Iterator<Quad<Vec3d, Integer, Float, Float>> radiusListIterator = radiusList.iterator();

        while(radiusListIterator.hasNext()) {
            Quad<Vec3d, Integer, Float, Float> quad = radiusListIterator.next();
            float radius = quad.fourth() * (MAX_RADIUS_SIZE - (currentTick * quad.third()) % MAX_RADIUS_SIZE);

            Vec3d rightArmOffset = getRightArmOffset(entity);

            DrawParticles.forWorld(serverWorld).inCircle(entity.getPos().add(rightArmOffset), quad.first(), radius, 1, ModParticles.QUIRK_OFA_CLOUD, 1, true);

            // Can try to earse them when radius is small
            if(quad.second() + MAX_CIRCLE_LIFE < currentTick || Math.abs(radius) < 0.2f) {
                radiusListIterator.remove();
            }
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void onTickInactive() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        if(ticksHeldFor > 5) {
            inActiveTicks++;

            if(posCache == null || vectorCache == null) {
                posCache = entity.getEyePos();
                vectorCache = entity.getRotationVector();
                entity.swingHand(Hand.MAIN_HAND, entity instanceof ServerPlayerEntity);
            }

            if(stage == STAGE_PRE_PUNCH)
                prePunchStage();
            else if(stage == STAGE_SHOCK_WAVES)
                shockWavesStage();

            if(inActiveTicks > 100) {
                ticksHeldFor = 0;
            }
        }

    }

    private void prePunchStage() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        for(int count = 1; count < ticksHeldFor; count++) {
            if(posCache != null && vectorCache != null) {
                Vec3d pos = posCache.add(vectorCache.multiply(count));
                DrawParticles.forWorld(serverWorld).spawnParticle(ParticleTypes.CLOUD, pos, new Vec3d(0, 0, 0), true);
            }
        }

        if(inActiveTicks > 30) {
            AnimationProxy.sendAnimationToClients(entity, "detroit_smash_release");
            stage = STAGE_SHOCK_WAVES;
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.OFA_RELEASE, SoundCategory.PLAYERS, 2f, 1f);
        }
    }

    private void shockWavesStage() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        if(!hasDisplayedShockwave) {
            for(int count = 1; count < ticksHeldFor - 1; count++) {
                if(posCache != null && vectorCache != null) {

                    Vec3d pos = posCache.add(vectorCache.multiply(count));
                    DrawParticles.forWorld(serverWorld).spawnParticles(ParticleTypes.CLOUD, pos, 20, new Vec3d(0, 0, 0), 1f, true);
                    DrawParticles.forWorld(serverWorld).spawnParticles(ParticleTypes.SWEEP_ATTACK, pos, 2, new Vec3d(2, 2, 2), 0f, true);
                    DrawParticles.forWorld(serverWorld).spawnParticles(ParticleTypes.EXPLOSION, pos, 2, new Vec3d(2, 2, 2), 0f, true);

                    if(count % 4 == 0) {
                        DrawParticles.forWorld(serverWorld).inCircle(pos, vectorCache, 4f, 3, ParticleTypes.CLOUD, 1, true);
                        serverWorld.createExplosion(entity, pos.getX(), pos.getY(), pos.getZ(), 3f, false, World.ExplosionSourceType.TNT);
                    }
                }
            }
            hasDisplayedShockwave = true;
        }
    }

    private Vec3d getRightArmOffset(LivingEntity livingEntity) {
        double yawRad = Math.toRadians(livingEntity.getBodyYaw() + 90);
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 1.75, z).normalize().multiply(0.75, 1, 0.75);
    }

    private void reset() {
        radiusList.clear();
        ticksHeldFor = 0;
        yawCache = 0;
        posCache = null;
        vectorCache = null;
        inActiveTicks = 0;
        hasDisplayedShockwave = false;
    }
}
