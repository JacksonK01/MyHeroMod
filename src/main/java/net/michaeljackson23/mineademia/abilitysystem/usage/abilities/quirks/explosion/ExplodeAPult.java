package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.PhaseAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.michaeljackson23.mineademia.util.RadiusMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ExplodeAPult extends PhaseAbility implements ICooldownAbility {

    public static final String DESCRIPTION = "While in mid-air, the user grabs an opponent with one of their arms, then fires an explosion with their free arm, causing both them and their opponent to spin. The user then forcefully throws his opponent with the aid of a second explosion.";

    public static final int COOLDOWN_TIME_MISS = 80;
    public static final int COOLDOWN_TIME_HIT = 300;

    public static final int STAMINA_COST_MISS = 50;
    public static final int STAMINA_COST_HIT = 100;

    public static final float DASH_PHASE_STRENGTH = 2f;

    public static final int GRAB_PHASE_TIME = 15;
    public static final float GRAB_RADIUS = 2f;

    public static final float THROW_HOLD_DISTANCE = 1.5f;
    public static final float THROW_RISE_STRENGTH = 3f;
    public static final float THROW_RECOIL_STRENGTH = 1f;
    public static final float THROW_TARGET_STRENGTH = 5f;

    public static final int VORTEX_LINES = 10;
    public static final float VORTEX_DENSITY = 0.1f;
    public static final float VORTEX_STEEPNESS = 200f;
    public static final float VORTEX_ROTATION_MULTIPLIER = 5f;
    public static final RadiusMap DASH_VORTEX_RADIUS = new RadiusMap(1);

    public static final int SMOKE_RECOIL_TIME = 20;
    public static final int SMOKE_RECOIL_DENSITY = 1;
    public static final float SMOKE_RECOIL_RADIUS = 5f;
    public static final float SMOKE_RECOIL_RADIUS_INCREASE = 1f;
    public static final float SMOKE_RECOIL_ROTATION_MULTIPLIER = 10f;

    public static final int TARGET_RING_FREQUENCY = 5;
    public static final int TARGET_RING_DENSITY = 1;
    public static final float TARGET_RING_RADIUS = 8f;
    public static final float TARGET_RING_RADIUS_INCREASE = 5f;
    public static final float TARGET_RING_ROTATION_MULTIPLIER = 10f;


    private final Cooldown cooldown;

    private LivingEntity target;

    private Vec3d endPos;
    private Vec3d endDirection;

    private double prevY;

    public ExplodeAPult(@NotNull IAbilityUser user) {
        super(user, "Explode-A-Pult", DESCRIPTION, Networking.C2S_ABILITY_FOUR, AbilityCategory.MOBILITY, AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN_TIME_MISS);

        setPhaseMethods(0, this::dashPhase, this::grabPhase, this::throwPhase, this::smokePhase);
        setStartPhaseMethods(0, this::startDashPhase, this::startGrabPhase, this::startThrowPhase);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!hasStamina(STAMINA_COST_HIT) || !isCooldownReady())
            return;
        if (getPhase() != -1)
            return;

        super.execute(isKeyDown);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }


    private void dashPhase() {

    }

    private void startDashPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d forward = entity.getRotationVecClient().normalize();

        entity.setVelocity(forward.multiply(DASH_PHASE_STRENGTH));
        entity.velocityModified = true;

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_HORSE_BREATHE, SoundCategory.MASTER, 3, 2);

        nextPhase();
    }


    private void grabPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        AffectAll<LivingEntity> nearbyEntities = AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), GRAB_RADIUS).exclude(entity);
        Optional<LivingEntity> target = nearbyEntities.getFirst();

        int ticks = getTicks();

        if (target.isPresent()) {
            this.target = target.get();

            cooldown.setCooldownTicks(COOLDOWN_TIME_HIT);
            nextPhase();
        } else if (ticks >= GRAB_PHASE_TIME) {
            cooldown.setCooldownTicks(COOLDOWN_TIME_MISS);

            resetCooldown();
            offsetStamina(-STAMINA_COST_MISS);

            resetPhase();
        }
    }

    private void startGrabPhase() {

    }


    private void throwPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d forward = entity.getRotationVecClient().normalize();

        int ticks = getTicks();

        if (ticks <= 5 || entity.getY() >= prevY) {
            Vec3d holdPosition = entity.getPos().add(forward.multiply(THROW_HOLD_DISTANCE));
            lockToPosition(target, holdPosition);
            prevY = entity.getY();

            DrawParticles.inVortex(world, this.endPos, Mathf.Vector.UP, DASH_VORTEX_RADIUS, ticks * VORTEX_ROTATION_MULTIPLIER, (int) (prevY - endPos.y - 1), VORTEX_LINES, VORTEX_DENSITY, VORTEX_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0, true);
        } else {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.DISTANT_EXPLOSION_2, SoundCategory.MASTER, 3, 2);
            target.setVelocity(forward.multiply(THROW_TARGET_STRENGTH));
            entity.setVelocity(forward.multiply(-THROW_RECOIL_STRENGTH));

            target.velocityModified = true;
            entity.velocityModified = true;

            resetCooldown();
            offsetStamina(-STAMINA_COST_HIT);

            IAbilityUser targetUser = AbilityManager.getUser(target);
            if (targetUser != null)
                targetUser.setBlocked(false);

            this.endPos = entity.getPos().add(forward.multiply(2));
            this.endDirection = forward;

            nextPhase();
        }
    }

    private void startThrowPhase() {
        LivingEntity entity = getEntity();

        entity.setVelocity(Mathf.Vector.UP.multiply(THROW_RISE_STRENGTH));
        entity.velocityModified = true;

        IAbilityUser targetUser = AbilityManager.getUser(target);
        if (targetUser != null)
            targetUser.setBlocked(true);

        this.endPos = entity.getPos();

        this.prevY  = entity.getY();
    }


    private void smokePhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        int ticks = getTicks();

        if (ticks <= SMOKE_RECOIL_TIME) {
            float radius = SMOKE_RECOIL_RADIUS + (ticks / (float) SMOKE_RECOIL_TIME) * SMOKE_RECOIL_RADIUS_INCREASE;
            DrawParticles.inCircle(world, this.endPos, this.endDirection, radius, ticks * SMOKE_RECOIL_ROTATION_MULTIPLIER, SMOKE_RECOIL_DENSITY, ParticleTypes.LARGE_SMOKE, true);
        } else
            resetPhase();

        if (ticks % TARGET_RING_FREQUENCY == 0) {
            float radius = (TARGET_RING_RADIUS + TARGET_RING_RADIUS_INCREASE) - (TARGET_RING_RADIUS + (ticks / (float) 40) * TARGET_RING_RADIUS_INCREASE);
            DrawParticles.inCircle(world, this.target.getPos(), this.target.getVelocity().normalize(), radius, ticks * TARGET_RING_ROTATION_MULTIPLIER, TARGET_RING_DENSITY, ModParticles.QUIRK_EXPLOSION_LONG, true);
            DrawParticles.inCircle(world, this.target.getPos(), this.target.getVelocity().normalize(), radius, ticks * TARGET_RING_ROTATION_MULTIPLIER, TARGET_RING_DENSITY, ParticleTypes.LARGE_SMOKE, true);
        }

        DrawParticles.spawnParticles(world, ModParticles.QUIRK_EXPLOSION_LONG, this.target.getPos(), 100, 0.25f, 0.25f, 0.25f, 0, true);
        DrawParticles.spawnParticles(world, ParticleTypes.LARGE_SMOKE, this.target.getPos(), 100, 0.25f, 0.25f, 0.25f, 0, true);
    }


    private void lockToPosition(@NotNull LivingEntity target, @NotNull Vec3d position) {
        Vec3d targetPos = target.getPos();

        Vec3d direction = position.subtract(targetPos).normalize();
        double distance = position.squaredDistanceTo(targetPos);

        if (distance <= 1)
            target.setVelocity(Vec3d.ZERO);
        else
            target.setVelocity(direction.multiply(distance / 40));

        target.velocityModified = true;
    }

}
