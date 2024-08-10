package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.michaeljackson23.mineademia.util.RaycastToEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class BallistaAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    private final Cooldown cooldown;

    private int timer = 0;

    private static final int DURATION = 80;
    private static final int POST_CHARGE_UP = 15;
    private static final int STAMINA_DRAIN = 100;
    private static final int COOLDOWN_TIME = 80;

    private boolean isActive = false;

    public BallistaAbility(@NotNull IAbilityUser user) {
        super(user, "Wind Ballista", "Charge a current of wind condensed in between your hands, once at max charge, all the wind will burst in the direction the user is looking.", AbilityCategory.ULTIMATE);
        cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public void onTick() {
        if(!isActive) {
            return;
        }
        timer++;

        if(timer > POST_CHARGE_UP) {
            windBlast();
        }

        if(timer >= DURATION) {
            isActive = false;
        }
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isCooldownReadyAndReset() && doesHaveRequiredStamina() && isKeyDown) {
            isActive = true;
            timer = 0;
            offsetStamina(-STAMINA_DRAIN);
            LivingEntity entity = getEntity();
            if(entity instanceof ServerPlayerEntity player) {
                AnimationProxy.sendAnimationToClients(player, "wind_ballista");
            }
        }
    }

    private boolean doesHaveRequiredStamina() {
        return getUser().getStamina() >= STAMINA_DRAIN;
    }

    private void windBlast()  {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        EntityHitResult entityHitResult = RaycastToEntity.raycast(entity, 15, (entity2, x, y, z) -> {
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    x, y, z,
                    10, 1, 1, 1, 0.05);
        });

        if(entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.setVelocity(entity.getRotationVector().x, 0.2, entity.getRotationVector().z);
            livingEntity.velocityModified = true;
            QuirkDamage.doEmitterDamage(entity, livingEntity, 9f);
        }

        entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
    }
}
