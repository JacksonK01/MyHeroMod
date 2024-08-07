package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

public class HowitzerImpactAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {

    public static final int COOLDOWN_TIME = 20 * 60 * 60; // 60 min

    public static final DrawParticles.VortexRadius[] TORNADO_BASE_RADIUS_MAP =  new DrawParticles.VortexRadius[] { new DrawParticles.VortexRadius(0, 7), new DrawParticles.VortexRadius(0.05f, 3), new DrawParticles.VortexRadius(0.8f, 0.5f), new DrawParticles.VortexRadius(1, 1.5f) };

    public static final int MAX_HEIGHT = 15;

    public static final int GLOW_RADIUS = 50;

    public static final int RANDOM_AMOUNT = 3;
    public static final float RANDOM_STRENGTH_MIN = 0.9f;
    public static final float RANDOM_STRENGTH_MAX = 1.1f;
    public static final float RANDOM_FREQUENCY = 10;

    public static final int TORNADO_LINE_COUNT = 10;
    public static final float TORNADO_STEP_SIZE = 0.1f;
    public static final float TORNADO_STEEPNESS = 200f;
    public static final float TORNADO_SPIN_MULTIPLIER = 5f;

    public static final float DUST_DISTANCE_MIN = 1;
    public static final float DUST_DISTANCE_MAX = 3;
    public static final int DUST_LINE_COUNT = 3;
    public static final float DUST_STEP_SIZE = 1f;
    public static final float DUST_STEEPNESS = 50f;
    public static final float DUST_SPIN_MULTIPLIER = 3f;

    public static final int SHOCKWAVE_HEIGHT = 7;
    public static final float SHOCKWAVE_MAX_SIZE = 10f;
    public static final float SHOCKWAVE_SLOW_MULTIPLIER = 2f;
    public static final float SHOCKWAVE_SPIN_MULTIPLIER = 5f;
    public static final int SHOCKWAVE_LINE_COUNT = 5;
    public static final float SHOCKWAVE_STEP_SIZE = 0.5f;
    public static final float SHOCKWAVE_STEEPNESS = 25f;

    public static final int RING_AMOUNT = 4;
    public static final float RING_DISTANCE = 3f;
    public static final float RING_SPEED = 0.1f;
    public static final float RING_FREQUENCY = 0.1f;
    public static final float RING_MULTIPLIER = 0.5f;
    public static final float RING_OFFSET = MAX_HEIGHT / (float) RING_AMOUNT;
    public static final int RING_DENSITY = 10;
    public static final float RING_SPIN_MULTIPLIER = 0.1f;

    public static final int RISE_PHASE_TIME = 200;
    public static final float RISE_PUSH_RADIUS = TORNADO_BASE_RADIUS_MAP[0].radius() + SHOCKWAVE_MAX_SIZE * 2;
    public static final float RISE_PUSH_MULTIPLIER = 0.011f;
    public static final float RISE_PUSH_SIDE_MULTIPLIER = 0.01f;
    public static final boolean RISE_PUSH_SET = false;

    public static final int DASH_PHASE_TIME = 100;
    public static final Vec3d DASH_MULTIPLIER = new Vec3d(1, 0.5f, 1);

    public static final DrawParticles.VortexRadius[] DASH_BEAM_RADIUS_MAP = new DrawParticles.VortexRadius[] { new DrawParticles.VortexRadius(0, 0), new DrawParticles.VortexRadius(1, 5) };
    public static final DrawParticles.VortexRadius[] DASH_TORNADO_RADIUS_MAP = new DrawParticles.VortexRadius[] { new DrawParticles.VortexRadius(0, 0), new DrawParticles.VortexRadius(1, 5) };

    public static final float SHOOT_STEP_SIZE = 1.5f;


    private final Cooldown cooldown;

    private int phase;
    private int ticks;

    private Vec3d pos;
    private Vec3d userPos;

    private DrawParticles.VortexRadius[] radiusMap;

    private final HashSet<Integer> glowingIds;

    private Vec3d projectilePos;
    private Vec3d projectileDirection;

    public HowitzerImpactAbility(@NotNull IAbilityUser user) {
        super(user, "Howitzer Impact", "The user dashes into the air and creates two Explosions in their hands. While in the air, the user spins themselves around, building up momentum for their Explosions. After spinning themselves around and gathering momentum for their Explosions, the user fires an Explosive tornado at their opponent.", AbilityCategory.ATTACK, AbilityCategory.ULTIMATE);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
        this.phase = -1;

        this.glowingIds = new HashSet<>();
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (phase >= 0 && !hasStaminaAndConsume(getUser().getMaxStamina()) && !isReadyAndReset())
            return;

        onRandomTick();

        this.phase = 0;
        this.ticks = 0;

        this.pos = getEntity().getPos();
        this.userPos = pos.add(0, MAX_HEIGHT, 0);
    }

    @Override
    public void onTick() {
        if (ticks % 10 == 0)
            removeGlow();

        if (phase >= 0)
            setNearbyGlow();

        risePhase();
        dashPhase();
        shootPhase();
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void risePhase() {
        if (phase != 0)
            return;

        lockToUserPosition();

        if (ticks % RANDOM_FREQUENCY == 0)
            onRandomTick();

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        DrawParticles.inVortex(world, pos, DrawParticles.UP, radiusMap, ticks * TORNADO_SPIN_MULTIPLIER, MAX_HEIGHT, TORNADO_LINE_COUNT, TORNADO_STEP_SIZE, TORNADO_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0);
        DrawParticles.inVortex(world, pos, DrawParticles.UP, DrawParticles.VortexRadius.addRandom(radiusMap, DUST_DISTANCE_MIN, DUST_DISTANCE_MAX), ticks * DUST_SPIN_MULTIPLIER, MAX_HEIGHT, DUST_LINE_COUNT, DUST_STEP_SIZE, DUST_STEEPNESS, ParticleTypes.SMOKE, Vec3d.ZERO, 1, 0);
        DrawParticles.inVortex(world, pos, DrawParticles.UP, DrawParticles.VortexRadius.constant(radiusMap[0].radius() + (ticks / SHOCKWAVE_SLOW_MULTIPLIER) % SHOCKWAVE_MAX_SIZE), ticks * SHOCKWAVE_SPIN_MULTIPLIER, SHOCKWAVE_HEIGHT, SHOCKWAVE_LINE_COUNT, SHOCKWAVE_STEP_SIZE, SHOCKWAVE_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 3, 0);

        float heightOffset = (ticks * RING_SPEED) % RING_OFFSET;

        for (int i = 1; i < RING_AMOUNT; i++) {
            float height = RING_OFFSET * i + heightOffset;
            float radius = DrawParticles.getVortexRadius(height / MAX_HEIGHT, TORNADO_BASE_RADIUS_MAP) + RING_DISTANCE + (float) Math.sin((ticks * RING_FREQUENCY) % 180) * RING_MULTIPLIER;

            Vec3d ringPos = pos.add(0, height, 0);
            DrawParticles.inCircle(world, ringPos, DrawParticles.UP, radius, ticks * RING_SPIN_MULTIPLIER, RING_DENSITY, ParticleRegister.QUIRK_EXPLOSION_BEAM);
        }

        AffectAll<Entity> affect = AffectAll.withinRadius(Entity.class, world, pos, RISE_PUSH_RADIUS).exclude(entity);

        affect.withVelocity(this::determinePushVelocity, RISE_PUSH_SET);

        if (ticks++ > RISE_PHASE_TIME) {
            phase++;
            ticks = 0;
        }
    }

    private void dashPhase() {
        if (phase != 1)
            return;

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d trailPos = entity.getPos().add(0, 1, 0);
        Vec3d backwards = entity.getRotationVecClient().multiply(-1).normalize();

        dashForward();



//        Vec3d frontPos = trailPos.add(backwards.multiply(-1));
//        BlockPos front = new BlockPos((int) frontPos.x, (int) frontPos.y, (int) frontPos.z);
//        if (!world.getBlockState(front).isAir())
//            System.out.println("HIT WALL");

        drawBeamTail(world, trailPos, backwards);
        DrawParticles.inVortex(world, trailPos, backwards, DASH_TORNADO_RADIUS_MAP, ticks * TORNADO_SPIN_MULTIPLIER, 20, TORNADO_LINE_COUNT, TORNADO_STEP_SIZE, TORNADO_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0);

        if (ticks++ > DASH_PHASE_TIME) {
            phase++;
            ticks = 0;
        }
    }

    private void shootPhase() {
        if (phase != 2)
            return;

        LivingEntity entity = getEntity();

        if (ticks++ == 0) {
            this.projectilePos = entity.getPos();
            this.projectileDirection = entity.getRotationVecClient().normalize();

            entity.setVelocity(this.projectileDirection.multiply(-5));
        }

        ServerWorld world = (ServerWorld) entity.getWorld();

        drawBeamTail(world, this.projectilePos, this.projectileDirection.multiply(-1));

        Vec3d nextPos = this.projectilePos.add(this.projectileDirection.multiply(SHOOT_STEP_SIZE));
        BlockPos nextBlock = new BlockPos((int) nextPos.x, (int) nextPos.y, (int) nextPos.z);

        if (!world.getBlockState(nextBlock).isAir()) {
            detonate(this.projectilePos);
            this.phase = -1;
        } else
            this.projectilePos = nextPos;
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

        ServerPlayNetworking.send(player, Networking.GLOW_ENTITIES, buffer);
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

        ServerPlayNetworking.send(player, Networking.GLOW_ENTITIES, buffer);

        glowingIds.clear();
    }

    private void onRandomTick() {
        this.radiusMap = new DrawParticles.VortexRadius[TORNADO_BASE_RADIUS_MAP.length + RANDOM_AMOUNT];
        float[] randoms = Mathf.randomArray(0, 1, RANDOM_AMOUNT);

        for (int i = 0; i < RANDOM_AMOUNT; i++) {
            float radius = DrawParticles.getVortexRadius(randoms[i], TORNADO_BASE_RADIUS_MAP) * Mathf.random(RANDOM_STRENGTH_MIN, RANDOM_STRENGTH_MAX);
            radiusMap[i] = new DrawParticles.VortexRadius(randoms[i], radius);
        }
        System.arraycopy(TORNADO_BASE_RADIUS_MAP, 0, radiusMap, RANDOM_AMOUNT, TORNADO_BASE_RADIUS_MAP.length);

        Arrays.sort(radiusMap, (a, b) -> Float.compare(a.partialHeight(), b.partialHeight()));
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

    @NotNull
    private Vec3d determinePushVelocity(@NotNull Entity affected) {
        Vec3d direction = affected.getPos().subtract(pos);
        double distance = direction.length();

        direction = direction.normalize().multiply((RISE_PUSH_RADIUS - distance) * RISE_PUSH_MULTIPLIER);

        Vec3d orthogonal = DrawParticles.getOrthogonal(direction);
        Vec3d up = direction.crossProduct(orthogonal);

        direction = direction.add(orthogonal.normalize().multiply((RISE_PUSH_RADIUS - distance) * RISE_PUSH_SIDE_MULTIPLIER)).add(up.normalize().multiply( 1 / 30f));

        return direction;
    }

    private void dashForward() {
        if (ticks % 2 == 0)
            return;

        LivingEntity entity = getEntity();

        Vec3d forward = entity.getRotationVecClient().normalize().multiply(DASH_MULTIPLIER);

        entity.setVelocity(forward);
        entity.velocityModified = true;
    }

    private void drawBeamTail(@NotNull ServerWorld world, @NotNull Vec3d pos, @NotNull Vec3d normal) {
        DrawParticles.inVortex(world, pos, normal, DASH_BEAM_RADIUS_MAP, ticks * 5, 20, 1, 0.1f, 1, ParticleRegister.QUIRK_EXPLOSION_BEAM, Vec3d.ZERO, 1, 0);
        DrawParticles.inVortex(world, pos, normal, DASH_BEAM_RADIUS_MAP, ((ticks + 90) * 5), 20, 1, 0.1f, 1, ParticleRegister.QUIRK_EXPLOSION_BEAM, Vec3d.ZERO, 1, 0);
        DrawParticles.inVortex(world, pos, normal, DASH_BEAM_RADIUS_MAP, ((ticks + 180) * 5), 20, 1, 0.1f, 1, ParticleRegister.QUIRK_EXPLOSION_BEAM, Vec3d.ZERO, 1, 0);
        DrawParticles.inVortex(world, pos, normal, DASH_BEAM_RADIUS_MAP, ((ticks + 270) * 5), 20, 1, 0.1f, 1, ParticleRegister.QUIRK_EXPLOSION_BEAM, Vec3d.ZERO, 1, 0);
    }

    private void detonate(@NotNull Vec3d pos) {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        DamageSource source = world.getDamageSources().explosion(entity, entity);
        world.createExplosion(entity, source, new ExplosionBehavior(), pos.getX(), pos.getY(), pos.getZ(), 50, true, World.ExplosionSourceType.MOB, ParticleRegister.EXPLOSION_QUIRK_PARTICLES, ParticleRegister.QUIRK_EXPLOSION_BEAM, SoundEvents.ENTITY_GENERIC_EXPLODE);

    }

}
