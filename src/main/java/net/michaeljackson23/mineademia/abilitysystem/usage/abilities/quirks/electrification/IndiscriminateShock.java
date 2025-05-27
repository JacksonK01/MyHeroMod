package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.electrification;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.PhaseAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//TODO add sound
public class IndiscriminateShock extends PhaseAbility implements ICooldownAbility {

    private static final int COOLDOWN = 100;
    private static final int STAMINA = 100;

    private static final int END_PHASE_CHARGE_TICKS = 50;
    private static final int END_PHASE_RELEASE_TICKS = 10;

    private static final float DAMAGE = 10f;

    private static final int RADIUS_WIDTH = 8;
    private static final int RADIUS_HEIGHT = 2;

    private static final int CHARGE_PARTICLE_COUNT = 3;
    private static final int RELEASE_PARTICLE_COUNT = 250;

    private final Cooldown cooldown;
    private final StatusEffectInstance taser = new StatusEffectInstance(StatusEffectsRegister.EFFECT_COWLING, 120, 0, true, false);

    private final BlockState lightBlock;
    private final BlockState air;
    private final List<BlockPos> placedLight;

    public IndiscriminateShock(@NotNull IAbilityUser user) {
        super(user, "Indiscriminate Shock 1.3 Million Volts", "User releases a large amount of electricity to shock everyone near him.", AbilityCategory.ATTACK);

        cooldown = new Cooldown(COOLDOWN);

        setPhaseMethods(0, this::chargePhase, this::releasePhase);

        lightBlock = Blocks.LIGHT.getDefaultState();
        placedLight = new ArrayList<>();
        air = Blocks.AIR.getDefaultState();
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown && cooldown.isReadyAndReset() && hasStaminaAndConsume(-STAMINA)) {
            nextPhase();
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void chargePhase() {
        int ticks = getTicks();

        ServerWorld world = getServerWorld();

        DrawParticles.forWorld(world).spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                getEntity().getPos().add(0, 1, 0), CHARGE_PARTICLE_COUNT, 0, 0, 0, -0.5f, true);

        BlockPos pos = getEntity().getBlockPos();

        if(world.getBlockState(pos).equals(air)) {
            world.setBlockState(pos, lightBlock);
            placedLight.add(pos);
        }

        if(ticks >= END_PHASE_CHARGE_TICKS) {
            nextPhase();
        }
    }

    private void releasePhase() {
        LivingEntity entity = getEntity();
        ServerWorld world = getServerWorld();
        int ticks = getTicks();

        DrawParticles.forWorld(getServerWorld()).spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                getEntity().getPos().add(0, 1, 0), RELEASE_PARTICLE_COUNT, RADIUS_WIDTH, RADIUS_HEIGHT, RADIUS_WIDTH, -0.5f, true);

        AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), RADIUS_WIDTH, RADIUS_HEIGHT, RADIUS_WIDTH).exclude(entity).with((toImpact) -> {
            toImpact.addStatusEffect(taser);
            QuirkDamage.doEmitterDamage(entity, toImpact, DAMAGE);
        });

        if(ticks >= END_PHASE_RELEASE_TICKS) {
            placedLight.forEach((pos) -> {
                world.setBlockState(pos, air);
            });
            placedLight.clear();
            resetPhase();
        }

    }

    @Override
    public void onStartTick() {
        super.onStartTick();
        if(getPhase() >= 1) {
            getServerWorld().setBlockState(getEntity().getBlockPos(), lightBlock);
        }
    }
}
