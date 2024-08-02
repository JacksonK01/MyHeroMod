package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ApShotAbility extends HoldAbility implements ICooldownAbility, IStaminaAbility {

    public static final int MAX_DURATION = 40;

    public static final int MAX_TICKS = 100;
    public static final int MAX_DISTANCE = 30;

    public static final float MIN_SIZE = 0f;
    public static final float MAX_SIZE = 0.75f;

    public static final float Y_OFFSET = 1f;
    public static final float PARTICLE_SPACING = 0.25f;


    private final Cooldown cooldown;

    private int ticks;
    private int endTicks;

    private float distance;
    private float duration;
    private float maxSize;

    public ApShotAbility(@NotNull IAbilityUser user) {
        super(user, "AP Shot", "The user stretches out one of their hands and uses their other hand to form a circle on the palm of their outstretched hand. By focusing the path of their explosions into a single point instead of around their whole palm, The user creates a concentrated blast with reduced area of impact.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(100);
        this.endTicks = MAX_DURATION + 1;
    }


    @Override
    public boolean executeStart() {
        if (!isReady())
            return false;

        getEntity().sendMessage(Text.literal("START"));

        ticks = 0;
        return true;
    }

    @Override
    public void executeEnd() {
        LivingEntity entity = getEntity();

        entity.sendMessage(Text.literal("Power: " + ticks));

        distance = ((float) ticks / MAX_TICKS) * MAX_DISTANCE;
        maxSize = MIN_SIZE + ((float) ticks / MAX_TICKS) * (MAX_SIZE - MIN_SIZE);
        duration = ((float) ticks / MAX_TICKS) * MAX_DURATION;

        getCooldown().setCooldownTicks(ticks);
        reset();

        endTicks = 0;
    }

    @Override
    public boolean onTickActive() {
        return ticks++ <= MAX_TICKS;
    }

    @Override
    public void onTickInactive() {
        if (endTicks <= duration) {
            drawBeam();
            endTicks++;
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 200;
    }

    private void drawBeam() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        HitResult hit = entity.raycast(distance, 1, true);
        Vec3d hitPos = hit.getPos();

        Vec3d current = entity.getPos().add(0, Y_OFFSET, 0);
        Vec3d direction = hitPos.subtract(current).normalize().multiply(PARTICLE_SPACING);

        float steps = 0;

        while (current.squaredDistanceTo(hitPos) > 1) {
            float delta = MIN_SIZE + (steps / distance) * (maxSize - MIN_SIZE);

            world.spawnParticles(ParticleRegister.EXPLOSION_QUIRK_PARTICLES, current.x, current.y, current.z , 100, delta, delta, delta, 0);
            current = current.add(direction);

            steps++;
        }
    }

}
