package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.PhaseAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class HowitzerImpactAbility extends PhaseAbility implements ICooldownAbility {

    public static final String DESCRIPTION = "The user dashes into the air and creates two Explosions in their hands. While in the air, the user spins themselves around, building up momentum for their Explosions. After spinning themselves around and gathering momentum for their Explosions, the user fires an Explosive tornado at their opponent.";

    public static final int COOLDOWN_TIME = 10; //20 * 60 * 60; // 60 min

    public static final int MAX_HEIGHT = 25;
    public static final int GLOW_RADIUS = 40;

    public static final int P1_TIME = 120;
    public static final int P2_TIME = 120;

    public static final boolean P1_PUSH_SET_VELOCITY = false;
    public static final float P1_PUSH_RADIUS = 20;
    public static final Vec3d P1_PUSH_AXIS_MULTIPLIER_VECTOR = new Vec3d(0.1, 0.1, 0.1); // (Forward, Side, Up)

    public static final int P1_VORTEX_LINES = 10;
    public static final float P1_VORTEX_DENSITY = 0.1f;
    public static final float P1_VORTEX_STEEPNESS = 200f;
    public static final float P1_VORTEX_ROTATION_MULTIPLIER = 5f;
    public static final RadiusMap P1_VORTEX_RADIUS = new RadiusMap(7f, 1.5f).put(0.05f, 3).put(0.8f, 0.5f).sort();

    public static final int P1_VORTEX_RANDOM_COUNT = 3;
    public static final int P1_VORTEX_RANDOM_FREQUENCY = 10;
    public static final float P1_VORTEX_RANDOM_STRENGTH_MIN = 0.9f;
    public static final float P1_VORTEX_RANDOM_STRENGTH_MAX = 1.1f;

    public static final int P1_DUST_DISTANCE_MIN = 1;
    public static final int P1_DUST_DISTANCE_MAX = 3;
    public static final int P1_DUST_LINES = 3;
    public static final float P1_DUST_DENSITY = 1f;
    public static final float P1_DUST_STEEPNESS = 50f;
    public static final float P1_DUST_ROTATION_MULTIPLIER = 3f;

    public static final int P1_SHOCKWAVE_HEIGHT = 7;
    public static final int P1_SHOCKWAVE_LINES = 5;
    public static final float P1_SHOCKWAVE_MAX_SIZE = 10f;
    public static final float P1_SHOCKWAVE_SPEED_MULTIPLIER = 2f;
    public static final float P1_SHOCKWAVE_DENSITY = 0.5f;
    public static final float P1_SHOCKWAVE_STEEPNESS = 25f;
    public static final float P1_SHOCKWAVE_ROTATION_MULTIPLIER = 5f;

    public static final int P1_RING_AMOUNT = 4;
    public static final int P1_RING_DENSITY = 10;
    public static final float P1_RING_DISTANCE = 3f;
    public static final float P1_RING_VERTICAL_SPEED = 0.1f;
    public static final float P1_RING_SINE_FREQUENCY = 0.1f;
    public static final float P1_RING_SINE_MULTIPLIER = 0.5f;
    public static final float P1_RING_SPIN_MULTIPLIER = 0.1f;
    public static final float P1_RING_HEIGHT_OFFSET = MAX_HEIGHT / (float) P1_RING_AMOUNT;

    public static final int P2_DASH_SOUND_LOOP_TIME = 20;

    public static final Vec3d P2_DASH_MULTIPLIER = new Vec3d(1, 0.75f, 1);

    public static final int P2_DASH_BEAM_AMOUNT = 4;
    public static final int P2_DASH_BEAM_ANGLE = 360 / P2_DASH_BEAM_AMOUNT;
    public static final int P2_DASH_BEAM_MAX_HEIGHT = 20;
    public static final float P2_DASH_BEAM_DENSITY = 0.1f;
    public static final float P2_DASH_BEAM_ROTATION_MULTIPLIER = 5f;
    public static final RadiusMap P2_DASH_BEAM_RADIUS = new RadiusMap(0, 5);

    public static final int P2_VORTEX_MAX_HEIGHT = 20;
    public static final int P2_VORTEX_LINES = 10;
    public static final float P2_VORTEX_DENSITY = 0.1f;
    public static final float P2_VORTEX_STEEPNESS = 200f;
    public static final float P2_VORTEX_ROTATION_MULTIPLIER = 5f;
    public static final RadiusMap P2_DASH_VORTEX_RADIUS = new RadiusMap(0, 5);

    public static final int P3_PROJECTILE_POWER = 50;
    public static final float P3_PROJECTILE_SPEED = 1f;
    public static final float P3_PROJECTILE_MAX_DISTANCE = 100f;
    public static final float P3_PROJECTILE_MAX_DISTANCE_SQUARED = P3_PROJECTILE_MAX_DISTANCE * P3_PROJECTILE_MAX_DISTANCE;

    public static final int P3_SMOKE_TIME = 20;
    public static final int P3_SMOKE_DENSITY = 1;
    public static final float P3_SMOKE_RECOIL = 0.5f;
    public static final float P3_SMOKE_RADIUS = 10f;
    public static final float P3_SMOKE_RADIUS_INCREASE = 5f;
    public static final float P3_SMOKE_ROTATION_MULTIPLIER = 5f;


    private final Cooldown cooldown;

    private int ticks;

    private Vec3d pos;
    private Vec3d userPos;

    private RadiusMap p1RandomizedRadius;

    private final HashSet<Integer> glowingIds;

    private Vec3d projectileStartPos;
    private Vec3d projectilePos;
    private Vec3d projectileDirection;

    public HowitzerImpactAbility(@NotNull IAbilityUser user) {
        super(user, "Howitzer Impact", DESCRIPTION, Networking.C2S_ABILITY_FIVE, AbilityCategory.ATTACK, AbilityCategory.MOBILITY, AbilityCategory.ULTIMATE);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
        this.glowingIds = new HashSet<>();

        setPhaseMethods(0, this::risePhase, this::dashPhase, this::shootPhase);
        setStartPhaseMethods(0, this::startRisePhase, this::startDashPhase, this::startShootPhase);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (this.getPhase() == 1 && isKeyDown) {
            this.nextPhase();
            this.ticks = 0;
            return;
        }
        else if (this.getPhase() >= 0 || !hasStamina(getUser().getMaxStamina()) || !isCooldownReadyAndReset())
            return;

        getUser().setStamina(0);

        super.execute(isKeyDown);
        this.ticks = 0;

        setBlockedCategories(AbilityCategory.all());

        this.pos = getEntity().getPos();
        this.userPos = pos.add(0, MAX_HEIGHT, 0);
    }

    @Override
    public void onStartTick() {
        if (ticks % 10 == 0)
            removeGlow();

        if (getPhase() >= 0)
            setNearbyGlow();

        super.onStartTick();

        ticks++;
    }

    @Override
    public void cancel() {
        super.cancel();

        this.ticks = 0;
        setBlockedCategories(AbilityCategory.none());
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void setNearbyGlow() {
        LivingEntity entity = getEntity();
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        World world = player.getWorld();

        PacketByteBuf buffer = PacketByteBufs.create();

        HashSet<LivingEntity> entities = AffectAll.withinRadius(LivingEntity.class, world, pos, GLOW_RADIUS).exclude(player).getAll();
        int[] ids = entities.stream().mapToInt(Entity::getId).toArray();

        for (int id : ids)
            glowingIds.add(id);

        buffer.writeIntArray(ids);
        buffer.writeBoolean(true);

        ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);
    }
    private void removeGlow() {
        if (glowingIds.isEmpty())
            return;

        LivingEntity entity = getEntity();
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeIntArray(glowingIds.stream().mapToInt((i) -> i).toArray());
        buffer.writeBoolean(false);

        ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);

        glowingIds.clear();
    }


    private void risePhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        lockToUserPosition();

        drawFirstPhase();

        AffectAll<Entity> affect = AffectAll.withinRadius(Entity.class, world, pos, P1_PUSH_RADIUS).exclude(entity);
        affect.withVelocity(this::determinePushVelocity, P1_PUSH_SET_VELOCITY);

        if (ticks > P1_TIME) {
            nextPhase();
            ticks = 0;
        }
    }

    private void startRisePhase() {
        if (getEntity() instanceof ServerPlayerEntity player)
            ServerPlayNetworking.send(player, Networking.S2C_FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
    }

    private void lockToUserPosition() {
        LivingEntity entity = getEntity();
        Vec3d entityPos = entity.getPos();

        Vec3d direction = userPos.subtract(entityPos).normalize();
        double distance = userPos.squaredDistanceTo(entityPos);

        if (distance <= 1)
            entity.setVelocity(Vec3d.ZERO);
        else
            entity.setVelocity(direction.multiply(distance / 40));

        entity.velocityModified = true;
    }

    private void drawFirstPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (ticks % P1_VORTEX_RANDOM_FREQUENCY == 0)
            this.p1RandomizedRadius = P1_VORTEX_RADIUS.randomizeMap(P1_VORTEX_RANDOM_COUNT, P1_VORTEX_RANDOM_STRENGTH_MIN, P1_VORTEX_RANDOM_STRENGTH_MAX);

        RadiusMap dustMap = p1RandomizedRadius.addRandom(P1_DUST_DISTANCE_MIN, P1_DUST_DISTANCE_MAX);

        float shockwaveRadius = p1RandomizedRadius.getRadius(0) + (ticks / P1_SHOCKWAVE_SPEED_MULTIPLIER) % P1_SHOCKWAVE_MAX_SIZE;
        RadiusMap shockwaveMap = new RadiusMap(shockwaveRadius);

        DrawParticles.inVortex(world, pos, Mathf.Vector.UP, p1RandomizedRadius, ticks * P1_VORTEX_ROTATION_MULTIPLIER, MAX_HEIGHT, P1_VORTEX_LINES, P1_VORTEX_DENSITY, P1_VORTEX_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0, true);
        DrawParticles.inVortex(world, pos, Mathf.Vector.UP, dustMap, ticks * P1_DUST_ROTATION_MULTIPLIER, MAX_HEIGHT, P1_DUST_LINES, P1_DUST_DENSITY, P1_DUST_STEEPNESS, ParticleTypes.SMOKE, Vec3d.ZERO, 1, 0, true);
        DrawParticles.inVortex(world, pos, Mathf.Vector.UP, shockwaveMap, ticks * P1_SHOCKWAVE_ROTATION_MULTIPLIER, P1_SHOCKWAVE_HEIGHT, P1_SHOCKWAVE_LINES, P1_SHOCKWAVE_DENSITY, P1_SHOCKWAVE_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 3, 0, true);

        float heightOffset = (ticks * P1_RING_VERTICAL_SPEED) % P1_RING_HEIGHT_OFFSET;

        for (int i = 1; i < P1_RING_AMOUNT; i++) {
            float height = P1_RING_HEIGHT_OFFSET * i + heightOffset;
            float radius = P1_VORTEX_RADIUS.getRadius(height / MAX_HEIGHT) + P1_RING_DISTANCE + (float) Math.sin((ticks * P1_RING_SINE_FREQUENCY) % 180) * P1_RING_SINE_MULTIPLIER;

            Vec3d ringPos = pos.add(0, height, 0);
            DrawParticles.inCircle(world, ringPos, Mathf.Vector.UP, radius, ticks * P1_RING_SPIN_MULTIPLIER, P1_RING_DENSITY, ModParticles.QUIRK_EXPLOSION_BEAM, true);
        }
    }

    @NotNull
    private Vec3d determinePushVelocity(@NotNull Entity affected) {
        Vec3d direction = affected.getPos().subtract(pos);

        double distance = direction.length();
        double relativeForce = P1_PUSH_RADIUS - distance;

        direction = direction.normalize().multiply(relativeForce * P1_PUSH_AXIS_MULTIPLIER_VECTOR.x);
        Vec3d side = Mathf.Vector.getOrthogonal(direction).multiply(relativeForce * P1_PUSH_AXIS_MULTIPLIER_VECTOR.y);
        Vec3d up = direction.crossProduct(side).normalize().multiply(relativeForce * P1_PUSH_AXIS_MULTIPLIER_VECTOR.z);

        return direction.add(side).add(up);
    }


    private void dashPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d trailPos = entity.getPos().add(0, 0, 0);

        Vec3d forward = entity.getRotationVecClient().normalize();
        Vec3d backward = forward.multiply(-1);

        dashForward();

        if (ticks % P2_DASH_SOUND_LOOP_TIME == 0)
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.BIG_TORNADO_LOOP, SoundCategory.MASTER, 1, (ticks / (float) P2_TIME));

        drawBeamTail(world, trailPos, backward);
        DrawParticles.inVortex(world, trailPos, backward, P2_DASH_VORTEX_RADIUS, ticks * P2_VORTEX_ROTATION_MULTIPLIER, P2_VORTEX_MAX_HEIGHT, P2_VORTEX_LINES, P2_VORTEX_DENSITY, P2_VORTEX_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0, true);

        Vec3d frontPos = trailPos.add(forward);
        BlockPos front = new BlockPos((int) frontPos.x, (int) frontPos.y, (int) frontPos.z);

        if (ticks > P2_TIME || !world.getBlockState(front).isAir()) {
            nextPhase();
            ticks = 0;
        }
    }

    private void startDashPhase() {
        if (getEntity() instanceof ServerPlayerEntity player) {
            EntityReflection.trySetLivingFlag(player, 4, true);
            ServerPlayNetworking.send(player, Networking.S2C_FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
        }
    }


    private void dashForward() {
        if (ticks % 2 == 0)
            return;

        LivingEntity entity = getEntity();

        Vec3d forward = entity.getRotationVecClient().normalize().multiply(P2_DASH_MULTIPLIER);

        entity.setVelocity(forward);
        entity.velocityModified = true;
    }

    private void drawBeamTail(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Vec3d normal) {
        for (int i = 0; i < P2_DASH_BEAM_AMOUNT; i++)
            DrawParticles.inVortex(world, pos, normal, P2_DASH_BEAM_RADIUS, ((ticks + (P2_DASH_BEAM_ANGLE * i)) * P2_DASH_BEAM_ROTATION_MULTIPLIER), P2_DASH_BEAM_MAX_HEIGHT, 1, P2_DASH_BEAM_DENSITY, 1, ModParticles.QUIRK_EXPLOSION_BEAM, Vec3d.ZERO, 1, 0, true);
    }


    private void shootPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (ticks <= P3_SMOKE_TIME) {
            Vec3d smokePlacement = this.projectileStartPos.add(this.projectileDirection.multiply(-P3_SMOKE_RECOIL));
            float radius = P3_SMOKE_RADIUS + (ticks / (float) P3_SMOKE_TIME) * P3_SMOKE_RADIUS_INCREASE;

            DrawParticles.inCircle(world, smokePlacement, this.projectileDirection, radius, ticks * P3_SMOKE_ROTATION_MULTIPLIER, P3_SMOKE_DENSITY, ParticleTypes.LARGE_SMOKE, true);
        }

        drawBeamTail(world, this.projectilePos, this.projectileDirection.multiply(-1));

        Vec3d nextPos = this.projectilePos.add(this.projectileDirection.multiply(P3_PROJECTILE_SPEED));
        BlockPos nextBlock = new BlockPos((int) nextPos.x, (int) nextPos.y, (int) nextPos.z);

        if (!world.getBlockState(nextBlock).isAir() || this.projectileStartPos.squaredDistanceTo(this.projectilePos) >= P3_PROJECTILE_MAX_DISTANCE_SQUARED) {
            detonate(this.projectilePos);
            resetPhase();
        } else
            this.projectilePos = nextPos;
    }

    private void startShootPhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (entity instanceof ServerPlayerEntity player)
            EntityReflection.trySetLivingFlag(player, 4, false);

        this.projectileStartPos = entity.getPos();
        this.projectilePos = entity.getPos();
        this.projectileDirection = entity.getRotationVecClient().normalize();

        AffectAll.withinRadius(world, this.projectileStartPos, 32).stopSound(ModSounds.BIG_TORNADO_LOOP, SoundCategory.MASTER);

        world.playSound(null, this.projectileStartPos.x, this.projectileStartPos.y, this.projectileStartPos.z, ModSounds.DISTANT_EXPLOSION_1, SoundCategory.MASTER, 10, 1);
        world.playSound(null, this.projectileStartPos.x, this.projectileStartPos.y, this.projectileStartPos.z, ModSounds.DISTANT_EXPLOSION_2, SoundCategory.MASTER, 10, 1);

        entity.setVelocity(this.projectileDirection.multiply(-5));
        entity.velocityModified = true;

        setBlockedCategories(AbilityCategory.none());
    }

    private void detonate(@NotNull Vec3d pos) {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        world.playSound(null, this.projectileStartPos.x, this.projectileStartPos.y, this.projectileStartPos.z, ModSounds.DEEP_EXPLOSION, SoundCategory.MASTER, 10, 1);

        DamageSource source = world.getDamageSources().explosion(entity, entity);
        world.createExplosion(entity, source, new ExplosionBehavior(), pos.getX(), pos.getY(), pos.getZ(), P3_PROJECTILE_POWER, true, World.ExplosionSourceType.MOB, ModParticles.QUIRK_EXPLOSION_SHORT, ModParticles.QUIRK_EXPLOSION_BEAM, SoundEvents.ENTITY_GENERIC_EXPLODE);

    }

}
