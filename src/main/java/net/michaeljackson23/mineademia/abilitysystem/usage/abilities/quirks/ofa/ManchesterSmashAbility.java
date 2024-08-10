package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ManchesterSmashAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    private static final int STAMINA = 210;
    private static final int COOLDOWN = 90;
    private static final int ABILITY_DURATION = 90;

    private static final int STAGE_START = 0;
    private static final int STAGE_LEAP = 1;
    private static final int STAGE_FLIP = 2;
    private static final int STAGE_DOWN = 3;

    private boolean isActivate = false;
    private int leapCounter = 0;
    private int timer = 0;
    private boolean hasLeapAnimationPlayed = false;
    private boolean hasManchesterAnimationPlayed = false;
    private int flipCounter = 0;
    private boolean impact = false;
    private boolean isAir = false;
    private boolean shouldStallInAir = false;
    private boolean hasGoneDown = false;
    private final Cooldown cooldown;

    //Stages
    //0 = START
    //1 = LEAP
    //2 = FLIP
    //3 = DOWN
    private int stage = 0;

    public ManchesterSmashAbility(@NotNull IAbilityUser user) {
        super(user, "Manchester Smash", "Leap into the air and flip forward to bring down an axe kick to your opponent.", AbilityCategory.ATTACK, AbilityCategory.ANIMATION, AbilityCategory.UTILITY);
        this.cooldown = new Cooldown(COOLDOWN);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown && isCooldownReadyAndReset() && hasStaminaAndConsume(STAMINA)) {
            init();
        }
    }

    private void init() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();
        stage = STAGE_START;

        isAir = world.getBlockState(entity.getBlockPos().down()).isAir();
        if(isAir) {
            stage = STAGE_FLIP;
            shouldStallInAir = true;
        } else {
            stage = STAGE_LEAP;
        }

        entity.velocityModified = true;

        isActivate = true;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public void onTick() {
        if(isActivate)
            manchesterSmash();
    }

    private void handleTimer() {
        timer++;
        if(timer >= ABILITY_DURATION) {
            executeWhenEnd();
        }
    }

    private void manchesterSmash() {
        handleTimer();
        if(stage == STAGE_LEAP)
            leapPhase();
        else if(stage == STAGE_FLIP)
            flipPhase();
         else if(stage == STAGE_DOWN)
             downPhase();
    }

    private void leapPhase() {
        LivingEntity livingEntity = getEntity();
        ServerWorld serverWorld = (ServerWorld) livingEntity.getWorld();
        Vec3d userPos = livingEntity.getPos().add(0, 0.2, 0);

        if(!hasLeapAnimationPlayed) {
            AnimationProxy.sendAnimationToClients(livingEntity, "charge_up_leap");
            hasLeapAnimationPlayed = true;

            livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.OFA_CHARGE, SoundCategory.PLAYERS, 2f, 1f);
            DrawParticles.spawnParticles(serverWorld, ParticleTypes.CLOUD,
                    userPos,
                    10, 0, 0, 0, 0.5f, true);
        }

        livingEntity.setVelocity(0, livingEntity.getVelocity().y, 0);

        leapCounter++;
        if(leapCounter > 8) {
            stage = STAGE_FLIP;
            livingEntity.setVelocity(0, 1.5, 0);
        }

        livingEntity.velocityModified = true;
    }

    private void flipPhase() {
        LivingEntity livingEntity = getEntity();
        ServerWorld serverWorld = (ServerWorld) livingEntity.getWorld();

        if(!hasManchesterAnimationPlayed) {
            AnimationProxy.sendAnimationToClients(livingEntity, "manchester_smash_shoot_style");
            serverWorld.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
            hasManchesterAnimationPlayed = true;
        }

        if(shouldStallInAir) {
            stallInAir();
        }

        flipCounter++;
        if(flipCounter >= 13) {
            stage = STAGE_DOWN;
        }
    }

    private void downPhase() {
        LivingEntity livingEntity = getEntity();
        ServerWorld serverWorld = (ServerWorld) livingEntity.getWorld();
        Vec3d userPos = livingEntity.getPos().add(0, 0.2, 0);

        if(!hasGoneDown) {
            goDown();
            hasGoneDown = true;
            serverWorld.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
            serverWorld.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), ModSounds.OFA_RELEASE, SoundCategory.PLAYERS, 2f, 1f);
            DrawParticles.spawnParticles(serverWorld, ParticleTypes.CLOUD,
                    userPos,
                    10, 0, 0, 0, 0.5f, true);
        }

        if(!(serverWorld.getBlockState(livingEntity.getBlockPos().down()).isAir())) {
            impact = true;
            executeWhenEnd();
        }

        if(livingEntity.getVelocity().y >= 0) {
            executeWhenEnd();
        }
    }
    //TODO add particles from the blocks under the spot of smash, and add block break noise
    private void onHitGround() {
        LivingEntity livingEntity = getEntity();
        ServerWorld serverWorld = (ServerWorld) livingEntity.getWorld();
        Vec3d userPos = livingEntity.getPos().add(0, 0.2, 0);

        livingEntity.setVelocity(livingEntity.getVelocity().x, 1, livingEntity.getVelocity().z);
        livingEntity.velocityModified = true;

        livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 2f, 2f);

        DrawParticles.spawnParticles(serverWorld, ParticleTypes.EXPLOSION,
                userPos,
                1, 0, 0, 0, 0, true);

        DrawParticles.spawnParticles(serverWorld, ParticleTypes.CAMPFIRE_COSY_SMOKE,
                userPos,
                25, 1, 0, 1, 0.5f, true);

        DrawParticles.spawnParticles(serverWorld, ParticleTypes.CLOUD,
                userPos,
                25, 1, 0, 1, 1, true);

        AffectAll.withinRadius(LivingEntity.class, serverWorld, livingEntity.getPos(), 4, 2, 4).exclude(livingEntity).with((entity) -> {
            entity.setVelocity(0, 1, 0);
            entity.velocityModified = true;
            QuirkDamage.doPhysicalDamage(livingEntity, entity, isAir ? 7.5f : 12.5f);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    1, 0, 0, 0, 0
            );
        });
    }

    private void stallInAir() {
        LivingEntity livingEntity = getEntity();

        livingEntity.setVelocity(livingEntity.getVelocity().x, 0.1, livingEntity.getVelocity().z);
        livingEntity.velocityModified = true;
    }

    private void goDown() {
        LivingEntity livingEntity = getEntity();

        Vec3d lookVec = livingEntity.getRotationVector().multiply(2, 0, 2).add(0, -2, 0);
        livingEntity.setVelocity(lookVec);
        livingEntity.velocityModified = true;
    }

    private void executeWhenEnd() {
        if(impact) {
            onHitGround();
        }
        AnimationProxy.sendStopAnimation(getEntity());
        timer = 0;
        leapCounter = 0;
        flipCounter = 0;
        isActivate = false;
        isAir = false;
        hasLeapAnimationPlayed = false;
        shouldStallInAir = false;
        hasManchesterAnimationPlayed = false;
        hasGoneDown = false;
        stage = STAGE_START;
        impact = false;
    }
}
