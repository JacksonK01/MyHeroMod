package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.damagetypes.CustomDamageTypes;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class ApShotAbility extends HoldAbility implements ICooldownAbility {

    public static final String DESCRIPTION = "The user stretches out one of their hands and uses their other hand to form a circle on the palm of their outstretched hand. By focusing the path of their explosions into a single point instead of around their whole palm, The user creates a concentrated blast with reduced area of impact.";


    public static final int MIN_COOLDOWN_TIME = 30;
    public static final int MAX_COOLDOWN_TIME = 300;

    public static final int STAMINA_PER_TICK = 3;

    public static final int MAX_FIRE_DURATION = 40;

    public static final int MAX_CHARGE_TIME = 60;
    public static final int MAX_DISTANCE = 20;

    public static final float MIN_SIZE = 0f;
    public static final float MAX_SIZE = 1.25f;

    public static final float MAX_CHARGE_SIZE = 0.1f;

    public static final float Y_OFFSET = 1f;
    public static final float PARTICLE_SPACING = 0.25f;

    public static final float DAMAGE = 10;

    public static final float HARDNESS_MULTIPLIER = 5f;


    private final Cooldown cooldown;

    private float distance;
    private float duration;
    private float maxSize;

    private final HashMap<Long, Integer> affectedBlocks;

    public ApShotAbility(@NotNull IAbilityUser user) {
        super(user, "AP Shot", DESCRIPTION, Networking.C2S_ABILITY_TWO, AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(MIN_COOLDOWN_TIME);
        this.affectedBlocks = new HashMap<>();
    }


    @Override
    public boolean executeStart() {
        if (!isCooldownReady())
            return false;

        affectedBlocks.clear();
        return true;
    }

    @Override
    public void executeEnd() {
        float partialHoldTime = (float) getTicks() / MAX_CHARGE_TIME;

        distance = Mathf.lerp(1, MAX_DISTANCE, partialHoldTime);
        maxSize = Mathf.lerp(MIN_SIZE, MAX_SIZE, partialHoldTime);
        duration = Mathf.lerp(1, MAX_FIRE_DURATION, partialHoldTime);

        int cooldown = (int) Mathf.lerp(MIN_COOLDOWN_TIME, MAX_COOLDOWN_TIME, partialHoldTime);

        getCooldown().setCooldownTicks(cooldown);
        resetCooldown();
    }

    @Override
    public void onTickActive() {
        if (getEntity() instanceof PlayerEntity player) {
            player.noClip = false;
        }

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        int ticks = getTicks();

        Vec3d forward = entity.getRotationVecClient().normalize().multiply(0.5f).add(0, Y_OFFSET, 0);
        Vec3d pos = entity.getPos().add(forward);

        float partialHoldTime = (float) ticks / MAX_CHARGE_TIME;
        float delta = Mathf.lerp(0, MAX_CHARGE_SIZE, partialHoldTime);

        world.spawnParticles(ModParticles.QUIRK_EXPLOSION_BEAM, pos.x, pos.y, pos.z , 10, delta, delta, delta, 0);

        if (ticks > MAX_CHARGE_TIME || !hasStamina((ticks + 1) * STAMINA_PER_TICK)) {
            executeEnd();
            setActive(false);
        }
    }

    @Override
    public void onTickInactive() {
        if (getEntity() instanceof PlayerEntity player) {
            player.noClip = false;
        }

        if (getTicks() <= duration) {
            drawBeam();
            offsetStamina(-STAMINA_PER_TICK);
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void drawBeam() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        HitResult hit = entity.raycast(distance, 1, true);
        Vec3d hitPos = hit.getPos();

        Vec3d current = entity.getPos().add(0, Y_OFFSET, 0);
        Vec3d direction = hitPos.subtract(current).normalize().multiply(PARTICLE_SPACING);

        HashSet<LivingEntity> affectedEntities = new HashSet<>();

        float steps = 0;

        while (current.squaredDistanceTo(hitPos) > 1) {
            float alpha = ((steps++ * PARTICLE_SPACING) / MAX_DISTANCE);
            float delta = Mathf.lerp(MIN_SIZE, maxSize, alpha);

            world.spawnParticles(ModParticles.QUIRK_EXPLOSION_BEAM, current.x, current.y, current.z , 100, delta, delta, delta, 0);

            Vec3d minBox = current.subtract(delta, delta, delta);
            Vec3d maxBox = current.add(delta, delta, delta);

            affectedEntities.addAll(world.getEntitiesByClass(LivingEntity.class, new Box(minBox, maxBox), (e) -> !e.equals(entity)));

            current = current.add(direction);
        }

        tickAffectedBlocks(world, hitPos);

        DamageSource source = world.getDamageSources().create(CustomDamageTypes.QUIRK_EXPLOSION_AP_SHOT, entity);
        for (LivingEntity affected : affectedEntities)
            affected.damage(source, DAMAGE);
    }

    private void tickAffectedBlocks(World world, Vec3d hitPos) {
        LivingEntity entity = getEntity();

        Vec3d minPos = hitPos.subtract(MAX_SIZE, MAX_SIZE, MAX_SIZE);
        Vec3d maxPos = hitPos.add(MAX_SIZE, MAX_SIZE, MAX_SIZE);

        BlockPos minBlock = new BlockPos((int) minPos.x, (int) minPos.y, (int) minPos.z);
        BlockPos maxBlock = new BlockPos((int) maxPos.x, (int) maxPos.y, (int) maxPos.z);


        Iterable<BlockPos> blocks = BlockPos.iterate(minBlock, maxBlock);

        for (BlockPos pos : blocks) {
            BlockState state = world.getBlockState(pos);
            if (state.isAir())
                continue;

            long posLong = pos.asLong();
            if (affectedBlocks.containsKey(posLong)) {
                int ticksAffected = affectedBlocks.get(posLong);

                if (ticksAffected >= state.getBlock().getHardness() * HARDNESS_MULTIPLIER) {
                    world.breakBlock(pos, true, entity);
                    affectedBlocks.remove(posLong);
                } else
                    affectedBlocks.put(posLong, ticksAffected + 1);
            } else
                affectedBlocks.put(posLong, 0);
        }

    }

    @Override
    public void onEndTick() {
        if (getEntity() instanceof PlayerEntity player && !isActive()) {
            player.noClip = false;
        }
    }

}
