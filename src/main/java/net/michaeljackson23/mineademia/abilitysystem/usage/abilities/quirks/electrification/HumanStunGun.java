package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.electrification;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HumanStunGun extends ActiveAbility implements ITickAbility, ICooldownAbility {

    private static final int MAX_TIMER = 100;
    private static final int COOLDOWN = 100;
    private static final int STAMINA = 100;

    private boolean isActive = false;
    private int counter = 0;
    private final Cooldown cooldown;


    private final StatusEffectInstance taser = new StatusEffectInstance(StatusEffectsRegister.EFFECT_COWLING, 90,0, true, false);

    public HumanStunGun(@NotNull IAbilityUser user) {
        super(user, "Human Stun Gun", "Defensively discharge electricity, electrocuting anyone who hits you.", AbilityCategory.DEFENSE);
        this.cooldown = new Cooldown(COOLDOWN);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown && cooldown.isReadyAndReset()) {
            offsetStamina(-STAMINA);
            isActive = true;
            spark();
        }
    }

    @Override
    public void onStartTick() {
        if(isActive) {
            run();
        }
    }

    private void run() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = getServerWorld();
        LivingEntity attacker = entity.getAttacker();

        counter++;

        if(counter >= MAX_TIMER) {
            if(entity instanceof ServerPlayerEntity player) {
                player.sendMessage(Text.literal("Failed to get a target").withColor(0xFFFF00), true);
            }
            serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1f, 2f);
            reset();
            return;
        }

        DrawParticles.forWorld(serverWorld).spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                entity.getPos().add(0, 1, 0), 1, 1, 1, 1, 0, true);

        if(counter % 5 == 0)
            serverWorld.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_ELECTRICITY_1, SoundCategory.PLAYERS, 0.1f, 1f);

        if(attacker == null) {
            return;
        }

        entity.setVelocity(findDistance(entity.getPos(), attacker.getPos()));
        entity.velocityModified = true;

        attacker.addStatusEffect(taser);
        QuirkDamage.doEmitterDamage(entity, attacker, 10f);
        DrawParticles.forWorld(serverWorld).spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                attacker.getPos().add(0, 1, 0), 10, 1f, 1f, 1f, 1, true);

        serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.PLAYERS, 1f, 0.5f);

        reset();
    }

    private Vec3d findDistance(Vec3d p1, Vec3d p2) {
        return p2.subtract(p1);
    }

    private void spark() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = getServerWorld();

        DrawParticles.forWorld(serverWorld).spawnParticles(ModParticles.ELECTRIFICATION_PARTICLES,
                entity.getPos().add(0, 1, 0), 5, 0.5f, 0.5f, 0.5f, 0, true);

        serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.PLAYERS, 1f, 2f);
        serverWorld.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_ELECTRICITY_2, SoundCategory.PLAYERS, 0.1f, 1f);
    }

    private void reset() {
        counter = 0;
        isActive = false;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }
}
