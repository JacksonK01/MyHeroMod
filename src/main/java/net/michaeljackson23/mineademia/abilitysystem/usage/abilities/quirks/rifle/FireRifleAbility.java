package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
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
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class FireRifleAbility extends HoldAbility implements ICooldownAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME = 0;

    public static final int SIGHT_GLOWING_COLOR = MathHelper.packRgb(1, 1, 0);
    public static final float SIGHT_GLOWING_RADIUS = 200f;
    public static final float SIGHT_ZOOM_LEVEL_DEFAULT = 0.3f;
    public static final float SIGHT_ZOOM_LEVEL_MIN = 0.25f;
    public static final float SIGHT_ZOOM_LEVEL_MAX = 0.75f;

    public static final int TARGET_LOCKED_COLOR = MathHelper.packRgb(1, 0 , 0);
    public static final int TARGET_LOCKING_COLOR = MathHelper.packRgb(1, 0.5f, 0);
    public static final int TARGET_TIME_TO_LOCK = 100;
    public static final int TARGET_TIME_TO_LOSE = 20;
    public static final float TARGET_ANGLE_TO_LOSE = 60f;
    public static final float TARGET_MAX_LOCK_DISTANCE = 200f;


    private final Cooldown cooldown;

    private final HashSet<Integer> glowingIds;

    private LivingEntity lockedTarget;
    private boolean locked;

    private float angleToTarget;
    private int timeOnTarget;
    private int timeOffTarget;

    private LoadAmmoAbility.AmmoType ammoType;
    private boolean ammoLoaded;

    private ArrowEntity projectile;
    private Vec3d lastShooterDirection;
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

        setZoom(SIGHT_ZOOM_LEVEL_DEFAULT, true);
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

        applyLockedZoom();

        applySightGlowing();
        applyTargetGlowing();

        drawShotTrajectory();
    }

    @Override
    public void onTickInactive() {
        if (!locked || lockedTarget == null || projectile == null || projectile.isRemoved() || projectile.isInsideWall())
            return;

        SuperchargedShotAbility superchargedShotAbility = getUser().getAbility(SuperchargedShotAbility.class);
        if (superchargedShotAbility == null)
            return;

        Vec3d directionToTarget = lastTargetPosition.subtract(projectile.getPos()).normalize();

        float anglesToTarget = (float) Math.abs(projectile.getVelocity().normalize().dotProduct(directionToTarget) - 1);
        float partial = Mathf.clamp(0, 1, anglesToTarget);

        if (Mathf.isZero(partial))
            partial = 1;

        projectile.setVelocity(Mathf.Vector.lerp(lastShooterDirection, directionToTarget, partial).normalize().multiply(ammoType.getVelocity() * superchargedShotAbility.getVelocityMultiplier()));
        projectile.velocityModified = true;

        // DrawParticles.spawnParticles((ServerWorld) getEntity().getWorld(), ParticleTypes.DRIPPING_LAVA, lastTargetPosition, 100, Mathf.Vector.ONE.multiply(0.5f), 0, true);
        if (projectile.getPos().squaredDistanceTo(lastTargetPosition) < 1) {
            // DrawParticles.spawnParticles((ServerWorld) getEntity().getWorld(), ParticleTypes.DRIPPING_HONEY, projectile.getPos(), 100, Mathf.Vector.ONE.multiply(0.5f), 0, true);
            projectile.kill();
            projectile = null;
        }
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


    private void lockOnTarget() {
        LivingEntity entity = getEntity();

        if (locked && lockedTarget != null) {
            Vec3d directionToTarget = lockedTarget.getPos().subtract(entity.getPos());
            double distance = directionToTarget.length();

            this.angleToTarget = Mathf.Vector.angleBetweenVectors(entity.getRotationVecClient().normalize(), directionToTarget.normalize());

            if (angleToTarget >= TARGET_ANGLE_TO_LOSE || distance >= TARGET_MAX_LOCK_DISTANCE) {
                setZoom(SIGHT_ZOOM_LEVEL_DEFAULT, true);
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
        if (lockedTarget == null || !locked || !(getEntity() instanceof ServerPlayerEntity))
            return;

        float partial = angleToTarget / TARGET_ANGLE_TO_LOSE;
        float zoomLevel = Mathf.lerp(SIGHT_ZOOM_LEVEL_MIN, SIGHT_ZOOM_LEVEL_MAX, partial);

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

    private void drawShotTrajectory() {
        if (locked && lockedTarget != null)
            drawCurvedShotTrajectory();
        else
            drawStraightShotTrajectory();
    }

    private void drawStraightShotTrajectory() {
//        if (!ammoLoaded || ammoType == null)
//            return;

//        LivingEntity entity = getEntity();
//        ServerWorld world = (ServerWorld) entity.getWorld();

//        Vec3d eyePos = entity.getEyePos();
//        Vec3d forward = entity.getRotationVecClient().normalize();

//        Vec3d spawnPosition = eyePos.add(forward);

        // DrawParticles.inLine(world, spawnPosition, eyePos.add(forward.multiply(ammoType.getRange())),0, 0.5f, 0, ModParticles.QUIRK_OFA_CLOUD, Vec3d.ZERO, 1, 0, true);
    }

    private void drawCurvedShotTrajectory() {
//        LivingEntity entity = getEntity();
//        ServerWorld world = (ServerWorld) entity.getWorld();

//        Vec3d eyePos = entity.getEyePos();
//        Vec3d forward = entity.getRotationVecClient().normalize();

        // Vec3d spawnPosition = eyePos.add(forward);
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

        projectile.setVelocity(forward.multiply(ammoType.getVelocity() * superchargedShotAbility.getVelocityMultiplier()));
        projectile.setNoGravity(true);
        projectile.setDamage(ammoType.getDamage() * superchargedShotAbility.getDamageMultiplier());

        world.spawnEntity(projectile);

        ammoLoaded = false;

        this.lastShooterDirection = forward;

        if (locked && lockedTarget != null)
            this.lastTargetPosition = lockedTarget.getPos();

        superchargedShotAbility.tryShootSupercharge();
    }

}
