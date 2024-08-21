package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.resonance;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class AerialStanceAbility extends ToggleAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 100;
    public static final int STAMINA_COST_PER_TICK = 0;

    public static final float STANCE_HEIGHT_MAX = 15f;
    public static final float STANCE_CEILING_SPACE = 2f;

    public static final float PUSH_RADIUS_LIVING = 40f;
    public static final float PUSH_LIVING_STRENGTH_MAX = 1f;
    public static final float PUSH_LIVING_STRENGTH_MIN = 0.00001f;

    public static final float PUSH_RADIUS_OTHER = 3f;
    public static final float PUSH_OTHER_STRENGTH = 5f;


    private final Cooldown cooldown;

    private Vec3d stancePos;

    public AerialStanceAbility(@NotNull IAbilityUser user) {
        super(user, "Aerial Stance", "", AbilityCategory.DEFENSE, AbilityCategory.ULTIMATE);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public boolean executeStart() {
        if (!isCooldownReady() || !hasStamina(STAMINA_COST_PER_TICK))
            return false;

        LivingEntity entity = getEntity();
        World world = entity.getWorld();

        Vec3d pos = entity.getPos();

        Vec3d ceilingHit = world.raycast(new RaycastContext(pos, pos.add(0, STANCE_HEIGHT_MAX + STANCE_CEILING_SPACE, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity)).getPos();
        Vec3d floorHit = world.raycast(new RaycastContext(pos, pos.add(0, -STANCE_CEILING_SPACE, 0), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity)).getPos();

        ceilingHit = new Vec3d(ceilingHit.x, ceilingHit.y - STANCE_CEILING_SPACE, ceilingHit.z);
        this.stancePos = ceilingHit.y > floorHit.y ? ceilingHit : floorHit;

        return true;
    }

    @Override
    public boolean executeEnd() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        AffectAll.withinRadius(Entity.class, world, stancePos, PUSH_RADIUS_OTHER).exclude(LivingEntity.class).withVelocity(this::pushOtherEntity, true);
        return true;
    }

    @Override
    public void onTickActive() {
        if (!hasStaminaAndConsume(STAMINA_COST_PER_TICK)) {
            setActive(false);
            return;
        }

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        ChangeFrequencyAbility frequencyAbility = getUser().getAbility(ChangeFrequencyAbility.class);
//        if (frequencyAbility != null) {
//            if (frequencyAbility.isConstructive())
//                DrawParticles.spawnParticles(world, ModParticles.QUIRK_RESONANCE_CONSTRUCTIVE, entity.getPos(), 100, new Vec3d(1, 1, 1), 0, true);
//            else
//                DrawParticles.spawnParticles(world, ModParticles.QUIRK_RESONANCE_DESTRUCTIVE, entity.getPos(), 100, new Vec3d(1, 1, 1), 0, true);
//        }

        lockToUserPosition();
    }

    @Override
    public void onEndTick() {
        if (isActive()) {
            LivingEntity entity = getEntity();
            ServerWorld world = (ServerWorld) entity.getWorld();

            AffectAll.withinRadius(LivingEntity.class, world, stancePos, PUSH_RADIUS_LIVING).exclude(entity).withVelocity(this::pushLivingEntity, false);
            AffectAll.withinRadius(Entity.class, world, stancePos, PUSH_RADIUS_OTHER).exclude(LivingEntity.class).withVelocity(this::suspendOtherEntity, true);
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void lockToUserPosition() {
        LivingEntity entity = getEntity();
        Vec3d entityPos = entity.getPos();

        Vec3d direction = stancePos.subtract(entityPos).normalize();
        double distance = stancePos.squaredDistanceTo(entityPos);

        if (distance <= 1)
            entity.setVelocity(Vec3d.ZERO);
        else
            entity.setVelocity(direction.multiply(distance / 40));

        entity.velocityModified = true;
    }

    @NotNull
    private Vec3d pushLivingEntity(@NotNull LivingEntity target) {
        Vec3d direction = target.getPos().subtract(stancePos);
        float distance = (float) direction.length();

        float partialPower = 1 - Mathf.clamp(0, 1, distance / PUSH_RADIUS_LIVING);
        float power = Mathf.lerp(PUSH_LIVING_STRENGTH_MIN, PUSH_LIVING_STRENGTH_MAX, partialPower);

        return direction.normalize().multiply(power);
    }

    @NotNull
    private Vec3d pushOtherEntity(@NotNull Entity target) {
        HitResult aimPosition = getEntity().raycast(PUSH_RADIUS_LIVING, 0, true);
        Vec3d direction = aimPosition.getPos().subtract(target.getPos());

        return direction.normalize().multiply(PUSH_OTHER_STRENGTH);
    }

    @NotNull
    private Vec3d suspendOtherEntity(@NotNull Entity target) {
        return Vec3d.ZERO;
    }

}
