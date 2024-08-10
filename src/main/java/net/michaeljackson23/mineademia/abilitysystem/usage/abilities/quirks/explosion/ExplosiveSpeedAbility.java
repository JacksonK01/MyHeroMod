package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ExplosiveSpeedAbility extends HoldAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME_MIN = 60;
    public static final int COOLDOWN_TIME_MAX = 120;

    public static final int STAMINA_PER_DASH = 2;

    public static final int DASH_MIN_FREQUENCY = 5;
    public static final int DASH_MAX_FREQUENCY = 10;
    public static final int DASH_TIME_TO_FULL_SPEED = 96;

    public static final int SOUND_START_TIME = 76;
    public static final int SOUND_LOOP_TIME = 20;

    public static final float VORTEX_START_TIME = 96;

    public static final int VORTEX_MAX_HEIGHT = 20;
    public static final int VORTEX_LINES = 10;
    public static final float VORTEX_DENSITY = 0.1f;
    public static final float VORTEX_STEEPNESS = 200f;
    public static final float VORTEX_ROTATION_MULTIPLIER = 5f;
    public static final float VORTEX_FORWARD_AMOUNT = 2;
    public static final RadiusMap DASH_VORTEX_RADIUS = new RadiusMap(1, 10);

    public static final int SMOKE_TIME = 5;
    public static final int SMOKE_DENSITY = 1;
    public static final float SMOKE_RADIUS = 3f;
    public static final float SMOKE_RADIUS_INCREASE = 3f;
    public static final float SMOKE_ROTATION_MULTIPLIER = 5f;

    public static final Vec3d DASH_STRENGTH = new Vec3d(1, 1, 1);


    private final Cooldown cooldown;

    private boolean drawSmoke;
    private Vec3d endPos;
    private Vec3d endDirection;

    public ExplosiveSpeedAbility(@NotNull IAbilityUser user) {
        super(user, "Explosive Speed", "The user throws their hands back and then causes an explosion in their palms to propel themselves forward. This move can also be used to achieve a flight.", AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME_MIN);
        setTicks(-1);
    }

    @Override
    public boolean executeStart() {
        setBlockedCategories(AbilityCategory.MOBILITY, AbilityCategory.ANIMATION);
        EntityReflection.trySetLivingFlag(getEntity(), 4, true);

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.SMALL_TORNADO_START, SoundCategory.MASTER, 1, 1);

        return hasStamina(STAMINA_PER_DASH) && isCooldownReady();
    }

    @Override
    public void executeEnd() {
        setBlockedCategories(AbilityCategory.none());
        EntityReflection.trySetLivingFlag(getEntity(), 4, false);

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        int ticks = getTicks();

        this.endPos = entity.getPos();
        this.endDirection = entity.getRotationVecClient().normalize();
        this.drawSmoke = ticks >= VORTEX_START_TIME;

        AffectAll.withinRadius(world, endPos, 32).stopSound(ModSounds.SMALL_TORNADO_START, SoundCategory.MASTER);
        AffectAll.withinRadius(world, endPos, 32).stopSound(ModSounds.SMALL_TORNADO_LOOP, SoundCategory.MASTER);

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.SMALL_TORNADO_END, SoundCategory.MASTER, 1, 1);

        float partial = Mathf.clamp(0, 1, ticks / (float) DASH_TIME_TO_FULL_SPEED);
        getCooldown().setCooldownTicks((int) Mathf.lerp(COOLDOWN_TIME_MIN, COOLDOWN_TIME_MAX, partial));

        resetCooldown();
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        int ticks = getTicks();

        float partial = 1 - Mathf.clamp(0, 1, ticks / (float) DASH_TIME_TO_FULL_SPEED);
        int frequency = (int) Mathf.lerp(DASH_MIN_FREQUENCY, DASH_MAX_FREQUENCY, partial);

        if (ticks >= SOUND_START_TIME && (ticks - SOUND_START_TIME) % SOUND_LOOP_TIME == 0)
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.SMALL_TORNADO_LOOP, SoundCategory.MASTER, 1, 1);

        if (ticks % frequency == 0) {
            moveUser();

           // world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSounds.SMALL_EXPLOSION, SoundCategory.MASTER, 1, 1);

            if (!hasStaminaAndConsume(STAMINA_PER_DASH)) {
                setActive(false);
                return;
            }
        }

        if (ticks >= VORTEX_START_TIME)
            drawVortex();
    }

    @Override
    public void onTickInactive() {
        int ticks = getTicks();

        if (drawSmoke && ticks <= SMOKE_TIME) {
            LivingEntity entity = getEntity();
            ServerWorld world = (ServerWorld) entity.getWorld();

            float radius = SMOKE_RADIUS + (ticks / (float) SMOKE_TIME) * SMOKE_RADIUS_INCREASE;
            DrawParticles.inCircle(world, this.endPos, this.endDirection, radius, ticks * SMOKE_ROTATION_MULTIPLIER, SMOKE_DENSITY, ParticleTypes.LARGE_SMOKE, true);
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void moveUser() {
        LivingEntity entity = getEntity();

        Vec3d velocity = entity.getRotationVecClient().normalize();

        velocity = velocity.normalize().multiply(DASH_STRENGTH);
        velocity = new Vec3d(velocity.x, velocity.y, velocity.z);

        entity.setVelocity(velocity);
        entity.velocityModified = true;
    }

    private void drawVortex() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d forward = entity.getRotationVecClient().normalize();
        Vec3d pos = entity.getPos().add(forward.multiply(VORTEX_FORWARD_AMOUNT));

        DrawParticles.inVortex(world, pos, forward.multiply(-1), DASH_VORTEX_RADIUS, getTicks() * VORTEX_ROTATION_MULTIPLIER, VORTEX_MAX_HEIGHT, VORTEX_LINES, VORTEX_DENSITY, VORTEX_STEEPNESS, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0, true);
    }

}
