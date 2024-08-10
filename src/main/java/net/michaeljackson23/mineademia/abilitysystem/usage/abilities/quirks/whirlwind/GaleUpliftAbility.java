package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class GaleUpliftAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    private static final int COOLDOWN_TIME = 40;
    private static final int STAMINA = 180;
    private static final int ABILITY_DURATION = 30;


    private final Cooldown cooldown;

    private boolean isActive;

    private int yScale;
    private int timer;

    public GaleUpliftAbility(@NotNull IAbilityUser user) {
        super(user, "Gale Uplift", "Use wind to bring enemies as high as possible to quickly slam them down.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public void onTick() {
        if(!isActive)
            return;

        Entity entity = getEntity();

        entity.setVelocity(0, 0, 0);
        entity.velocityModified = true;

        if (timer <= ABILITY_DURATION - 1)
            handleAscendingPhase();
        else {
            handleDescendingPhase();
            isActive = false;
        }

        yScale++;
        timer++;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(!isCooldownReadyAndReset() && isKeyDown && getStamina() >= STAMINA) {
            return;
        }
        offsetStamina(-STAMINA);
        isActive = true;
        LivingEntity user = getUser().getEntity();
        if(user instanceof ServerPlayerEntity player) {
            AnimationProxy.sendAnimationToClients(player, "whirlwind_up_down");
        }
        this.timer = 0;
        this.yScale = 0;
        user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 0.55f);
    }

    private void handleAscendingPhase() {
        LivingEntity entity = getEntity();

        spawnCloudParticles(10, 0.4, 0.01);

        // AreaOfEffect.execute(entity, 4, yScale, entity.getX(), entity.getY(), entity.getZ(), this::applyAscendingEffects);
        AffectAll.withinRadius(LivingEntity.class, entity.getWorld(), entity.getPos(), 4).exclude(entity).with(this::applyDescendingEffects);
    }

    private void handleDescendingPhase() {
        LivingEntity entity = getEntity();

        spawnCloudParticles(25, 0.4, 1);
        entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 2f);

        // AreaOfEffect.execute(entity, 4, yScale, entity.getX(), entity.getY(), entity.getZ(), this::applyDescendingEffects);
        AffectAll.withinRadius(LivingEntity.class, entity.getWorld(), entity.getPos(), 4, yScale, 4).exclude(entity).with(this::applyDescendingEffects);
    }

    private void spawnCloudParticles(int count, double spread, double speed) {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        serverWorld.spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), count, spread, 0, spread, speed);
    }

    private void applyAscendingEffects(LivingEntity target) {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        target.setVelocity(0, 0.2, 0);
        target.velocityModified = true;

        serverWorld.spawnParticles(ParticleTypes.CLOUD, target.getX(), target.getY() + 1, target.getZ(), 10, 0.3, 0.3, 0.3, 0.01);
    }

    private void applyDescendingEffects(LivingEntity target) {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        Vec3d entityRotation = entity.getRotationVector();

        QuirkDamage.doEmitterDamage(entity, target, 15f);

        target.setVelocity(entityRotation.x, -1, entityRotation.z);
        target.velocityModified = true;

        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY() + 1, target.getZ(), 1, 0, 0, 0, 0);
        serverWorld.spawnParticles(ParticleTypes.CLOUD, target.getX(), target.getY() + 1, target.getZ(), 50, 0.3, 0.3, 0.3, 0.5);
    }

}
