package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class HowitzerImpactAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {

    public static final int COOLDOWN_TIME = 10;// 20 * 60 * 2; // 2 min

    public static final int MAX_HEIGHT = 15;

    public static final float START_TORNADO_RADIUS = 5;
    public static final float CENTER_TORNADO_RADIUS = 2f;
    public static final float END_TORNADO_RADIUS = 4;

    public static final int TORNADO_LINES = 10;

    public static final float TORNADO_HEIGHT_INCREASE = 0.5f;

    public static final float TORNADO_CENTER_POINT = 0.8f;

    public static final int TORNADO_SPEED = 3;

    public static final float TORNADO_OUTLINE_HEIGHT_INCREASE = TORNADO_HEIGHT_INCREASE * 5;
    public static final float TORNADO_OUTLINE_SIZE = 3;
    public static final int TORNADO_OUTLINE_DENSITY = 15;

    public static final float TORNADO_OUTLINE_MIN_SPEED = 15;
    public static final float TORNADO_OUTLINE_MAX_SPEED = 25;

    public static final float TORNADO_OUTLINE_MIN_MOVEMENT = 50;
    public static final float TORNADO_OUTLINE_MAX_MOVEMENT = 100;


    private final Cooldown cooldown;
    private int phase;


    private int phase1Ticks;

    private final float[] tornadoOutlineSpeed;
    private final float[] tornadoOutlineHeights;

    public HowitzerImpactAbility(@NotNull IAbilityUser user) {
        super(user, "Howitzer Impact", "The user dashes into the air and creates two Explosions in their hands. While in the air, the user spins themselves around, building up momentum for their Explosions. After spinning themselves around and gathering momentum for their Explosions, the user fires an Explosive tornado at their opponent.", AbilityCategory.ATTACK, AbilityCategory.ULTIMATE);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
        this.phase = -1;

        this.tornadoOutlineSpeed = new float[(int) (MAX_HEIGHT / TORNADO_HEIGHT_INCREASE)];
        this.tornadoOutlineHeights = new float[(int) (MAX_HEIGHT / TORNADO_HEIGHT_INCREASE)];
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!isReadyAndReset())
            return;

        createOverlaySpeeds();
        normal = new Vec3d(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1);
        pos = getEntity().getPos();
        this.phase = 0;
    }

    @Override
    public void onTick() {
        risePhase();
        hoverPhase();
        attackPhase();
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private Vec3d normal;
    private Vec3d pos;

    private void risePhase() {
        if (phase != 0)
            return;

        ServerWorld world = (ServerWorld) getEntity().getWorld();

//        tornadoBody(world, pos, phase1Ticks * TORNADO_SPEED);
//        tornadoOverlay(world, pos, phase1Ticks * TORNADO_SPEED);

        // DrawParticles.inCircle(world, pos, normal, 5, phase1Ticks, 15, ParticleTypes.CLOUD);
        DrawParticles.VortexRadius[] radiusMap = new DrawParticles.VortexRadius[] { new DrawParticles.VortexRadius(0, 5), new DrawParticles.VortexRadius(0.8f, 3), new DrawParticles.VortexRadius(1, 5) };

        DrawParticles.inVortex(world, pos, normal, radiusMap, phase1Ticks * 3, 15, 10, 0.25f, 25, ParticleTypes.CLOUD, Vec3d.ZERO, 1, 0);
  //      DrawParticles.inVortex(world, pos, new Vec3d(1, 0, 0), radiusMap, phase1Ticks * 3, 10, 4, 0.25f, 10, ParticleTypes.LARGE_SMOKE, Vec3d.ZERO, 1, 0);
//        DrawParticles.inVortex(world, pos, new Vec3d(1, 0, 0), radiusMap, phase1Ticks * 3, 10, 4, 0.25f, 20, ParticleTypes.FLAME, Vec3d.ZERO, 1, 0);

        if (phase1Ticks++ >= 2000) {
            phase = -1;
            phase1Ticks = 0;
        }
    }

    private void hoverPhase() {
        if (phase != 1)
            return;
    }

    private void attackPhase() {
        if (phase != 2)
            return;
    }

    private void tornadoBody(ServerWorld world, Vec3d pos, float angle) {
        for (int l = 0; l < TORNADO_LINES; l++) {
            for (float y = 0; y < MAX_HEIGHT; y += TORNADO_HEIGHT_INCREASE) {
                double radius = getRadius(y, 0, -1);
                double radians = Math.toRadians(360d / TORNADO_LINES * l + y * 25 - angle);

                double x = Math.cos(radians) * radius;
                double z = Math.sin(radians) * radius;

                world.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.x + x, pos.y + y, pos.z + z, 1, 0, 0, 0, 0);
            }
        }
    }

    private void tornadoOverlay(ServerWorld world, Vec3d pos, int time) {
        int index = 0;

        for (float y = TORNADO_OUTLINE_HEIGHT_INCREASE; y < MAX_HEIGHT - TORNADO_OUTLINE_HEIGHT_INCREASE; y += TORNADO_OUTLINE_HEIGHT_INCREASE) {
            float offsetY = y + (float) Math.sin(time / tornadoOutlineHeights[index]) * 0.75f;
            double radius = getRadius(offsetY, time, index);

            for (int i = 0; i < 360; i += TORNADO_OUTLINE_DENSITY) {
                double radians = Math.toRadians(i - time);

                double x = Math.cos(radians) * radius;
                double z = Math.sin(radians) * radius;

                world.spawnParticles(ParticleRegister.EXPLOSION_QUIRK_PARTICLES, pos.x + x, pos.y + offsetY, pos.z + z, 1, 0, 0, 0, 0);
            }

            index++;
        }
    }

    private double getRadius(float y, float time, int index) {
        float partialHeight = y / MAX_HEIGHT;//+ (float) Math.sin(time / tornadoOutlineHeights[index]);

        double radius;
        if (partialHeight <= TORNADO_CENTER_POINT)
            radius = Mathf.lerp(START_TORNADO_RADIUS + TORNADO_OUTLINE_SIZE, CENTER_TORNADO_RADIUS + TORNADO_OUTLINE_SIZE, partialHeight * (1 / TORNADO_CENTER_POINT));
        else
            radius = Mathf.lerp(CENTER_TORNADO_RADIUS + TORNADO_OUTLINE_SIZE, END_TORNADO_RADIUS + TORNADO_OUTLINE_SIZE, (partialHeight - TORNADO_CENTER_POINT) * (1 / (1 - TORNADO_CENTER_POINT)));

        if (index >= 0)
            radius += Math.sin(time / tornadoOutlineSpeed[index]);

        return radius;
    }

    private void createOverlaySpeeds() {
        int index = 0;

        for (float y = TORNADO_OUTLINE_HEIGHT_INCREASE; y < MAX_HEIGHT - TORNADO_OUTLINE_HEIGHT_INCREASE; y += TORNADO_OUTLINE_HEIGHT_INCREASE) {
            tornadoOutlineSpeed[index] = Mathf.random(TORNADO_OUTLINE_MIN_SPEED, TORNADO_OUTLINE_MAX_SPEED);
            tornadoOutlineHeights[index] = Mathf.random(TORNADO_OUTLINE_MIN_MOVEMENT, TORNADO_OUTLINE_MAX_MOVEMENT);

            index++;
        }
    }

}
