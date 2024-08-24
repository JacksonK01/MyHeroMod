package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.stickyfingers;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.NoClipAbility;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

public class SurfaceZippingAbility extends HoldAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 1;
    public static final int INSTANT_CLICK_TIME = 5;

    public static final float ZIPPER_START_PLACE_RANGE = 5f;
    public static final float ZIPPER_END_PLACE_RANGE = 10f;

    public static final float ZIPPER_SINE_RADIUS = 2f;
    public static final float ZIPPER_STEP_SIZE = 0.25f;


    private final Cooldown cooldown;

    private Vec3d heldStart;

    private Vec3d selectedStart;
    private Vec3d selectedEnd;

    private Vec3d selectedNormal;
    private Vec3d drawingNormal;

    private Vec3d directionStart;
    private Vec3d directionEnd;

    private boolean zipperOpen;

    private final HashSet<Long> affectedBlocks;

    public SurfaceZippingAbility(@NotNull IAbilityUser user) {
        super(user, "Surface Zipping", "", Networking.C2S_ABILITY_ONE, AbilityCategory.UTILITY, AbilityCategory.MOBILITY, AbilityCategory.ULTIMATE);

        this.cooldown = new Cooldown(COOLDOWN_TIME);

        this.affectedBlocks = new HashSet<>();
    }


    @Override
    public boolean executeStart() {
        LivingEntity entity = getEntity();

        HitResult result = entity.raycast(ZIPPER_START_PLACE_RANGE, 0, false);
        if (result.getType() == HitResult.Type.BLOCK && result.getPos() != null) {
            this.heldStart = result.getPos();
            this.directionStart = entity.getRotationVecClient();
        } else
            this.heldStart = null;

        return true;
    }

    @Override
    public void executeEnd() {
        if (getTicks() <= INSTANT_CLICK_TIME) {
            LivingEntity entity = getEntity();

            if (entity.isSneaking())
                destroyZipper();
            else
                toggleZipper();
        } else
            createZipper();
    }

    @Override
    public void onTickActive() {
        tickZipper();
        tickPlannedZipper();
    }

    @Override
    public void onTickInactive() {
        tickZipper();

//        AffectAll.withinRadius(Entity.class, getEntity().getWorld(), getEntity().getPos(), 16, 128, 16).exclude(LivingEntity.class).with(Entity::tick);
//        AffectAll.withinRadius(Entity.class, getEntity().getWorld(), getEntity().getPos(), 16, 128, 16).exclude(LivingEntity.class).with(Entity::tick);
//        AffectAll.withinRadius(Entity.class, getEntity().getWorld(), getEntity().getPos(), 16, 128, 16).exclude(LivingEntity.class).with(Entity::tick);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    public boolean canPhase(@NotNull BlockPos pos) {
        if (getEntity().getWorld().getBlockState(pos).isAir())
            return true;

        return affectedBlocks.contains(pos.asLong());
    }

    @Nullable
    public NoClipAbility getNoClipAbility() {
        return getUser().getAbility(NoClipAbility.class);
    }

    public boolean canPhase(@NotNull Box box) {
        for (double x = box.minX; x <= box.maxX; x++) {
            for (double y = box.minY; y <= box.maxY; y++) {
                for (double z = box.minZ; z <= box.maxZ; z++) {
                    BlockPos pos = BlockPos.ofFloored(x, y, z);
                    if (!canPhase(pos))
                        return false;
                }
            }
        }

        return true;
    }

    public boolean hasZipper() {
        return selectedStart != null && selectedEnd != null && !affectedBlocks.isEmpty();
    }

    private void createZipper() {
        if (this.heldStart == null)
            return;

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        HitResult result = entity.raycast(ZIPPER_END_PLACE_RANGE, 0, false);
        if (result.getType() != HitResult.Type.BLOCK || result.getPos() == null)
            return;

        if (!canCreateZipper(heldStart, result.getPos()))
            return;

        if (hasZipper())
            destroyZipper();

        this.selectedStart = this.heldStart;
        this.selectedEnd = result.getPos();

        this.directionEnd = entity.getRotationVecClient();
        getZipperNormals();

        this.zipperOpen = false;

        Vec3d normalStep = selectedNormal.multiply(ZIPPER_STEP_SIZE);

        Vec3d start = selectedStart.add(normalStep);
//        start = new Vec3d((int) start.x, (int) start.y, (int) start.z);

        Vec3d end = selectedEnd.add(normalStep);
//        end = new Vec3d((int) end.x, (int) end.y, (int) end.z);

        Vec3d step = end.subtract(start).normalize().multiply(ZIPPER_STEP_SIZE);
        Vec3d current = start;

        double maxDistance = end.distanceTo(start);
        double distance = maxDistance;
        double prevDistance = maxDistance;

        affectedBlocks.clear();
        while (distance <= prevDistance) {
            double partial = 1 - (distance / maxDistance);
            double radians = partial * Math.PI;

            double radius = Math.sin(radians) * ZIPPER_SINE_RADIUS;

            Vec3d edge1 = current.add(drawingNormal.multiply(radius));
            Vec3d edge2 = current.add(drawingNormal.multiply(-radius));

//            BlockPos blockCurrent = Mathf.getBlock(current);
            BlockPos blockEdge1 = Mathf.getBlock(edge1);
            BlockPos blockEdge2 = Mathf.getBlock(edge2);

//            DrawParticles.spawnParticles(world, ParticleTypes.DRAGON_BREATH, blockCurrent.toCenterPos(), 10, Mathf.Vector.ONE.multiply(0.2f), 0, true);
            for (BlockPos block : BlockPos.iterate(blockEdge1, blockEdge2)) {
//                DrawParticles.spawnParticles(world, ParticleTypes.CLOUD, block.toCenterPos(), 10, Mathf.Vector.ONE.multiply(0.2f), 0, true);
                affectedBlocks.add(block.asLong());
            }

//            DrawParticles.spawnParticles(world, ParticleTypes.DRAGON_BREATH, edge1, 1, Vec3d.ZERO, 0, true);
//            DrawParticles.spawnParticles(world, ParticleTypes.DRAGON_BREATH, edge2, 1, Vec3d.ZERO, 0, true);

            current = current.add(step);
            prevDistance = distance;
            distance = current.distanceTo(end);
        }

        NoClipAbility noClipAbility = getNoClipAbility();
        if (noClipAbility != null) {
            noClipAbility.setNoClipCondition(this::canPhase);
            noClipAbility.setEntityCondition((e) -> canPhase(e.getBoundingBox()));
        }
    }

    private void destroyZipper() {
        this.selectedStart = null;
        this.selectedEnd = null;

        this.zipperOpen = false;

        this.affectedBlocks.clear();


        NoClipAbility noClipAbility = getNoClipAbility();
        if (noClipAbility != null)
            noClipAbility.setNoClipping(false);
    }

    private void getZipperNormals() {
        Vec3d direction = this.selectedEnd.subtract(this.selectedStart);
        Vec3d v1 = Mathf.Vector.getOrthogonal(direction);

        Vec3d avgDirection = directionStart.add(directionEnd).normalize();
        System.out.println(avgDirection);
        System.out.println(v1.dotProduct(avgDirection));
        if (v1.dotProduct(avgDirection) < 0) {
            System.out.println("FLIPPED NORMAL");
            v1 = v1.multiply(-1);
        }

        Vec3d v2 = direction.crossProduct(v1).normalize();

        System.out.println(selectedStart);
        System.out.println(selectedEnd);

        if (direction.x == (int) direction.x) {
            this.selectedNormal = v1;
            this.drawingNormal = v2;
        } else if (direction.y == (int) direction.y) {
            this.selectedNormal = v2;
            this.drawingNormal = v1;
        } else if (direction.z == (int) direction.z) {
            this.selectedNormal = v1;
            this.drawingNormal = v2;
        }
    }


    private void toggleZipper() {
        if (hasZipper()) {
            zipperOpen = !zipperOpen;


            NoClipAbility noClipAbility = getNoClipAbility();
            if (noClipAbility != null)
                noClipAbility.setNoClipping(zipperOpen);
        }

    }

    private void tickZipper() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (!hasZipper())
            return;

        if (zipperOpen) {
            Vec3d step = selectedEnd.subtract(selectedStart).normalize().multiply(ZIPPER_STEP_SIZE);
            Vec3d current = selectedStart;

            double maxDistance = selectedEnd.distanceTo(selectedStart);
            double distance = maxDistance;
            double prevDistance = maxDistance;

            while (distance <= prevDistance) {
                double partial = 1 - (distance / maxDistance);
                double radians = partial * Math.PI;

                double radius = Math.sin(radians) * ZIPPER_SINE_RADIUS;

                Vec3d edge1 = current.add(drawingNormal.multiply(radius));
                Vec3d edge2 = current.add(drawingNormal.multiply(-radius));

                // DrawParticles.inLine(world, edge1, edge2, 0, 0.2f, 0, ParticleTypes.DRAGON_BREATH, Vec3d.ZERO, 1, 0, true);

//                DrawParticles.spawnParticles(world, ParticleTypes.DRAGON_BREATH, edge1, 1, Vec3d.ZERO, 0, true);
//                DrawParticles.spawnParticles(world, ParticleTypes.DRAGON_BREATH, edge2, 1, Vec3d.ZERO, 0, true);

                current = current.add(step);
                prevDistance = distance;
                distance = current.distanceTo(selectedEnd);
            }
        } else {
            // DrawParticles.inLine(world, selectedStart, selectedEnd, 0, 0.2f, 0, ParticleTypes.FLAME, Vec3d.ZERO, 1, 0, true);

//            Vec3d current = selectedStart;
//            Vec3d step = selectedEnd.subtract(selectedStart).normalize().multiply(0.2f);
//
//            double maxDistance = selectedEnd.squaredDistanceTo(selectedStart);
//            Vec3d normal1 = Mathf.Vector.getOrthogonal(step);
//            Vec3d normal2 = step.crossProduct(normal1).normalize();
//
//            while (current.squaredDistanceTo(selectedEnd) >= 0.2f * 0.2f) {
//                double partial = current.squaredDistanceTo(selectedEnd) / maxDistance;
//                double radian = partial * Math.PI;
//
//                double radius = Math.sin(radian) * 2;
//
//                DrawParticles.spawnParticles(world, ParticleTypes.FLAME, current.add(normal2.multiply(radius)), 1, Vec3d.ZERO, 0, true);
//                DrawParticles.spawnParticles(world, ParticleTypes.FLAME, current.add(normal2.multiply(-radius)), 1, Vec3d.ZERO, 0, true);
//                current = current.add(step);
//            }
        }
    }

    private void tickPlannedZipper() {
        if (heldStart == null)
            return;

        LivingEntity entity = getEntity();
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        ServerWorld world = (ServerWorld) entity.getWorld();

        HitResult result = entity.raycast(ZIPPER_END_PLACE_RANGE, 0, false);
        if (result.getType() != HitResult.Type.BLOCK || result.getPos() == null)
            return;

        boolean canCreate = canCreateZipper(heldStart, result.getPos());
        // DrawParticles.inLine(world, heldStart, result.getPos(), 0, 0.2f, 0, canCreate ? ParticleTypes.ELECTRIC_SPARK : ParticleTypes.MYCELIUM, Vec3d.ZERO, 1, 0, true);
    }

    private boolean canCreateZipper(@NotNull Vec3d start, @NotNull Vec3d end) {
        Vec3d direction = end.subtract(start);

        if (direction.x == (int) direction.x) {
            return true;
        } else if (direction.y == (int) direction.y) {
            return true;
        } else return direction.z == (int) direction.z;
    }


    @NotNull
    private ArrayList<Long> getNeighbours(@NotNull BlockPos pos, int maxIterations, int iterations) {
        ArrayList<Long> result = new ArrayList<>();
        result.add(pos.asLong());

        if (iterations >= maxIterations)
            return result;

        result.addAll(getNeighbours(pos.up(), maxIterations, iterations + 1));
        result.addAll(getNeighbours(pos.down(), maxIterations, iterations + 1));

//        result.addAll(getNeighbours(pos.north(), maxIterations, iterations + 1));
//        result.addAll(getNeighbours(pos.south(), maxIterations, iterations + 1));
//        result.addAll(getNeighbours(pos.east(), maxIterations, iterations + 1));
//        result.addAll(getNeighbours(pos.west(), maxIterations, iterations + 1));

        return result;
    }

}
