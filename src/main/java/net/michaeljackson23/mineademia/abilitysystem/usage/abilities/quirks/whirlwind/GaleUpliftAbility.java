package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class GaleUpliftAbility extends ActiveAbility implements IStaminaAbility, ICooldownAbility, ITickAbility {
    int yScale = 0;
    private final Cooldown cooldown;
    private int timer = 0;
    private boolean isActive = false;

    private static final int ABILITY_DURATION = 30;

    public GaleUpliftAbility(@NotNull IAbilityUser user) {
        super(user, "Gale Uplift", "Use wind to bring enemies as high as possible to quickly slam them down.", AbilityCategory.ATTACK);

        cooldown = new Cooldown(40);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 100;
    }

    @Override
    public void onTick() {
        if(!isActive) {
            return;
        }
        yScale++;
        timer++;
        getEntity().setVelocity(0, 0, 0);
        getEntity().velocityModified = true;
        if (timer <= ABILITY_DURATION - 1) {
            handleAscendingPhase(getEntity());
        } else {
            handleDescendingPhase(getEntity());
            isActive = false;
        }
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(!isReadyAndReset()) {
            return;
        }
        isActive = true;
        LivingEntity user = getUser().getEntity();
        if(user instanceof ServerPlayerEntity player) {
            AnimationProxy.sendAnimationToClients(player, "whirlwind_up_down");
        }
        this.timer = 0;
        this.yScale = 0;
        user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 0.55f);
    }

    private void handleAscendingPhase(LivingEntity user) {
        spawnCloudParticles(user, 10, 0.4, 0.01);
        AreaOfEffect.execute(user, 4, yScale, user.getX(), user.getY(), user.getZ(), entityToAffect -> {
            applyAscendingEffects(user, entityToAffect);
        });
    }

    private void handleDescendingPhase(LivingEntity user) {
        spawnCloudParticles(user, 25, 0.4, 1);
        AreaOfEffect.execute(user, 4, yScale, user.getX(), user.getY(), user.getZ(), entityToAffect -> {
            applyDescendingEffects(user, entityToAffect);
        });
        user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 2f, 2f);
    }

    private void spawnCloudParticles(LivingEntity user, int count, double spread, double speed) {
        MinecraftServer server = user.getServer();
        if(server != null) {
            ServerWorld serverWorld = (ServerWorld) user.getWorld();
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    user.getX(), user.getY(), user.getZ(),
                    count, spread, 0, spread, speed);
        }
    }

    private void applyAscendingEffects(LivingEntity user, LivingEntity entityToAffect) {
        entityToAffect.setVelocity(0, 0.2, 0);
        entityToAffect.velocityModified = true;
        MinecraftServer server = user.getServer();
        if(server != null) {
            ServerWorld serverWorld = (ServerWorld) user.getWorld();
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                    10, 0.3, 0.3, 0.3, 0.01);
        }
    }

    private void applyDescendingEffects(LivingEntity user, LivingEntity entityToAffect) {
        QuirkDamage.doEmitterDamage(user, entityToAffect, 15f);
        entityToAffect.setVelocity(user.getRotationVector().x, -1, user.getRotationVector().z);
        entityToAffect.velocityModified = true;
        MinecraftServer server = user.getServer();
        if(server != null) {
            ServerWorld serverWorld = (ServerWorld) user.getWorld();
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                    1, 0, 0, 0, 0);
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(),
                    50, 0.3, 0.3, 0.3, 0.5);
        }
    }
}
