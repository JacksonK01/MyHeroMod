package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IRightClickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class FireRifleAbility extends HoldAbility implements ICooldownAbility, IRightClickAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME = 0;

    public static final int SIGHT_GLOWING_COLOR = MathHelper.packRgb(1, 1, 0);
    public static final float SIGHT_GLOWING_RADIUS = 200f;

    public static final float[] SIGHT_ZOOM_LEVELS = new float[] { 0.75f, 0.5f, 0.25f };

    public static final float SIGHT_ZOOM_TARGET_MIN = 0.25f;
    public static final float SIGHT_ZOOM_TARGET_MAX = 0.75f;

    public static final int TARGET_LOCKED_COLOR = MathHelper.packRgb(1, 0 , 0);
    public static final int TARGET_LOCKING_COLOR = MathHelper.packRgb(1, 0.5f, 0);
    public static final int TARGET_TIME_TO_LOCK = 1; // 100
    public static final int TARGET_TIME_TO_LOSE = 20;
    public static final float TARGET_ANGLE_TO_LOSE = 89f; // 60
    public static final float TARGET_MAX_LOCK_DISTANCE = 200f;

    public static final float PROJECTILE_INITIAL_ALPHA = 0.1f;
    public static final float PROJECTILE_STRAIGHT_VELOCITY = 7f;

    public static final float AIRWALK_RECOIL_MULTIPLIER = 1.5f;


    private final Cooldown cooldown;
    private int zoomLevel;

    private final HashSet<Integer> glowingIds;

    private LivingEntity lockedTarget;
    private boolean locked;

    private float angleToTarget;
    private int timeOnTarget;
    private int timeOffTarget;

    private LoadAmmoAbility.AmmoType ammoType;
    private boolean ammoLoaded;

    private ArrowEntity projectile;

    private float curvedShotAlpha;
    private float projectileAlphaIncrement;
    private Vec3d lastShooterPosition;
    private Vec3d lastPivotPoint;
    private Vec3d lastTargetPosition;

    public FireRifleAbility(@NotNull IAbilityUser user) {
        super(user, "Fire Rifle", DESCRIPTION, Networking.C2S_ABILITY_ONE, AbilityCategory.ATTACK, AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);

        this.glowingIds = new HashSet<>();
    }

    @Override
    public boolean executeStart() {
        lockedTarget = null;
        locked = false;

        timeOnTarget = 0;
        timeOffTarget = 0;

        zoomLevel = -1;
        // setZoom(SIGHT_ZOOM_LEVEL_DEFAULT, true);
        return true;
    }

    @Override
    public void executeEnd() {
        setZoom(1, false);
        removeGlow();

        if (ammoLoaded)
            fireRifle();
    }

    @Override
    public void onTickActive() {
        if (getTicks() % 10 == 0)
            removeGlow();

        lockOnTarget();

        lastShooterPosition = getEntity().getEyePos();
        if (locked && lockedTarget != null) {
            lastTargetPosition = lockedTarget.getEyePos();
            lastPivotPoint = getPivotPoint();
        }

        applyLockedZoom();

        applySightGlowing();
        applyTargetGlowing();
        applyProjectileGlowing();

        drawCurvedShotTrajectory();
    }

    @Override
    public void onTickInactive() {
        tickProjectile();
        applyProjectileGlowing();
    }

    @Override
    public boolean onRightClick(boolean isKeyDown) {
        if (!isActive() || !isKeyDown)
            return false;

        boolean isSneaking = getEntity().isSneaking();

        if (isSneaking)
            zoomLevel--;
        else
            zoomLevel++;

        if (zoomLevel >= SIGHT_ZOOM_LEVELS.length)
            zoomLevel = -1;
        else if (zoomLevel < -1)
            zoomLevel = SIGHT_ZOOM_LEVELS.length - 1;

        setDefaultZoom();
        return true;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    public boolean isAmmoLoaded() {
        return ammoLoaded;
    }

    public void setAmmoType(@NotNull LoadAmmoAbility.AmmoType ammoType) {
        this.ammoType = ammoType;
    }

    public void setAmmoLoaded(boolean ammoLoaded) {
        this.ammoLoaded = ammoLoaded;
    }

    private void setZoom(float zoomLevel, boolean smoothCamera) {
        if (!(getEntity() instanceof ServerPlayerEntity player))
            return;

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(zoomLevel);
        buffer.writeBoolean(smoothCamera);

        ServerPlayNetworking.send(player, Networking.S2C_ZOOM, buffer);
    }

    private void setDefaultZoom() {
        setZoom(zoomLevel == -1 ? 1 : SIGHT_ZOOM_LEVELS[zoomLevel], zoomLevel != -1);
    }


    private void lockOnTarget() {
        LivingEntity entity = getEntity();

        if (locked && lockedTarget != null) {
            Vec3d directionToTarget = lockedTarget.getPos().subtract(entity.getPos());
            double distance = directionToTarget.length();

            this.angleToTarget = Mathf.Vector.angleBetweenVectors(entity.getRotationVecClient().normalize(), directionToTarget.normalize());

            if (angleToTarget >= TARGET_ANGLE_TO_LOSE || distance >= TARGET_MAX_LOCK_DISTANCE) {
                setDefaultZoom();
                lockedTarget = null;
                locked = false;

                angleToTarget = 0;
                timeOffTarget = 0;
                timeOnTarget = 0;
            }

            return;
        }

        if (lockedTarget != null) {
            if (timeOffTarget++ >= TARGET_TIME_TO_LOSE) {
                lockedTarget = null;

                angleToTarget = 0;
                timeOffTarget = 0;
                timeOnTarget = 0;
            }
        }

        EntityHitResult hit = Raycast.raycastEntity(entity, TARGET_MAX_LOCK_DISTANCE);
        if (hit == null)
            return;

        Entity target = hit.getEntity();
        if (!(target instanceof LivingEntity livingTarget))
            return;

        if (lockedTarget == null)
            lockedTarget = livingTarget;
        else if (livingTarget != lockedTarget)
            return;

        timeOffTarget = 0;

        if (timeOnTarget++ >= TARGET_TIME_TO_LOCK) {
            locked = true;

            timeOnTarget = 0;
        }
    }

    private void applyLockedZoom() {
        if (!(getEntity() instanceof ServerPlayerEntity))
            return;

        if (lockedTarget == null || !locked)
            return;

        float partial = angleToTarget / TARGET_ANGLE_TO_LOSE;
        float zoomLevel = Mathf.lerp(SIGHT_ZOOM_TARGET_MIN, SIGHT_ZOOM_TARGET_MAX, partial);

        setZoom(zoomLevel, false);
    }

    private void applyTargetGlowing() {
        if (lockedTarget == null || !(getEntity() instanceof ServerPlayerEntity player))
            return;

        int id = lockedTarget.getId();

        GlowingHelper.setColor(lockedTarget, locked ? TARGET_LOCKED_COLOR : TARGET_LOCKING_COLOR);
        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeIntArray(new int[] { id });
        buffer.writeBoolean(true);

        glowingIds.add(id);

        ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);
    }

    private void applySightGlowing() {
        if (!(getEntity() instanceof ServerPlayerEntity player))
            return;

        ServerWorld world = (ServerWorld) player.getWorld();

        AffectAll.withinRadius(LivingEntity.class, world, player.getPos(), SIGHT_GLOWING_RADIUS).exclude(player)
                .insertInto(glowingIds, Entity::getId)
                .withGlowingColor(SIGHT_GLOWING_COLOR)
                .withClientGlowing(player, true);
    }

    private void applyProjectileGlowing() {
        if (getEntity() instanceof ServerPlayerEntity player && projectile != null) {
            PacketByteBuf buffer = PacketByteBufs.create();

            buffer.writeIntArray(new int[] { projectile.getId() });
            buffer.writeBoolean(true);

            glowingIds.add(projectile.getId());

            GlowingHelper.setColor(projectile, 1, 0, 1);

            ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);
        }
    }

    private void drawCurvedShotTrajectory() {
        if (!locked || lockedTarget == null || !(getEntity() instanceof ServerPlayerEntity player))
            return;

        float alpha = 0;
        float alphaIncrease = 1f / (float) (lastShooterPosition.distanceTo(lastPivotPoint) + lastPivotPoint.distanceTo(lastTargetPosition));

        ServerWorld world = (ServerWorld) player.getWorld();
        boolean isPathClear = true;

        DrawParticles drawer = DrawParticles.forPlayer(player);

        while (alpha <= 1) {
            Vec3d point = Mathf.Vector.lerp(alpha, lastShooterPosition, lastPivotPoint, lastTargetPosition);
            BlockPos pos = BlockPos.ofFloored(point.x, point.y, point.z);

            if (!world.getBlockState(pos).isAir()) {
                isPathClear = false;
                break;
            }

            alpha += alphaIncrease;
        }

        drawer.lerpBetween(isPathClear ? ParticleTypes.CRIT : ParticleTypes.FLAME, 10, Vec3d.ZERO, 0, true, 0, 0.1f, 0.1f, lastShooterPosition, lastPivotPoint, lastTargetPosition);
    }

    private void removeGlow() {
        LivingEntity entity = getEntity();

        if (glowingIds.isEmpty() || !(entity instanceof ServerPlayerEntity player))
            return;

        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeIntArray(glowingIds.stream().mapToInt((i) -> i).toArray());
        buffer.writeBoolean(false);

        ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);

        glowingIds.clear();
    }


    private void fireRifle() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (entity.isSneaking())
            return;

        SuperchargedShotAbility superchargedShotAbility = getUser().getAbility(SuperchargedShotAbility.class);
        if (superchargedShotAbility == null)
            return;

        Vec3d eyePos = entity.getEyePos();
        Vec3d forward = entity.getRotationVecClient().normalize();

        Vec3d spawnPosition = eyePos.add(forward);

        ItemStack projectileItem = new ItemStack(Items.ARROW, 1);
        this.projectile = new ArrowEntity(entity.getWorld(), spawnPosition.x, spawnPosition.y, spawnPosition.z, projectileItem);

        projectile.setOwner(entity);
        projectile.setDamage(superchargedShotAbility.getDamage(ammoType));
        projectile.setPierceLevel(superchargedShotAbility.getPierce(ammoType));

        this.ammoLoaded = false;
        this.lastShooterPosition = eyePos;

        world.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_RIFLE_SHOOT, SoundCategory.MASTER, 0.5f, 1);

        if (locked && lockedTarget != null) {
            this.curvedShotAlpha = PROJECTILE_INITIAL_ALPHA;
            this.projectileAlphaIncrement = (1f - PROJECTILE_INITIAL_ALPHA) / ammoType.getTicksToHit();
            this.lastTargetPosition = lockedTarget.getEyePos();
            this.lastPivotPoint = getPivotPoint();
        } else {
            projectile.setNoGravity(true);
            projectile.setVelocity(forward.multiply(PROJECTILE_STRAIGHT_VELOCITY));
        }

        world.spawnEntity(projectile);

        superchargedShotAbility.tryShootSupercharge();


        AirwalkAbility airwalkAbility = getUser().getAbility(AirwalkAbility.class);
        if (airwalkAbility == null)
            return;

        if (airwalkAbility.isActive() && !entity.isOnGround()) {
            entity.addVelocity(forward.multiply(-AIRWALK_RECOIL_MULTIPLIER));
            entity.velocityModified = true;
        }
    }

    private void tickProjectile() {
        if (!locked || lockedTarget == null || projectile == null || projectile.isRemoved() || projectile.isInsideWall() || curvedShotAlpha > 1)
            return;

        Vec3d desiredPoint = Mathf.Vector.lerp(curvedShotAlpha, lastShooterPosition, lastPivotPoint, lastTargetPosition);
        Vec3d directionToPoint = desiredPoint.subtract(projectile.getEyePos());

        projectile.setVelocity(directionToPoint);
        projectile.velocityModified = true;

        curvedShotAlpha += projectileAlphaIncrement;
    }

    @NotNull
    private Vec3d getPivotPoint() {
        LivingEntity entity = getEntity();

        Vec3d entityPos = entity.getEyePos();
        Vec3d forward = entity.getRotationVecClient().normalize();

        Vec3d directionToTarget = lastTargetPosition.subtract(entityPos).normalize();
        Vec3d centerPoint = lastTargetPosition.add(entityPos).multiply(0.5f);

        Vec3d planeNormal = forward.crossProduct(directionToTarget).normalize();
        Vec3d centerCross = directionToTarget.crossProduct(planeNormal).normalize();

        float alpha = Mathf.Vector.angleBetweenVectors(forward, directionToTarget);
        float dist = (float) (Math.tan(alpha * Mathf.Deg2Rad) * entityPos.distanceTo(centerPoint));

        return centerPoint.add(centerCross.multiply(dist));
    }

}
