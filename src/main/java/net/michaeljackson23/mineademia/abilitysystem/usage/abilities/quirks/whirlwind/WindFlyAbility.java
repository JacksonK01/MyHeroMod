package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class WindFlyAbility extends ToggleAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 5;
    private static final int PASSIVE_STAMINA_USE = 1;


    private final Cooldown cooldown;

    public WindFlyAbility(@NotNull IAbilityUser user) {
        super(user, "Fly", "User's wind creates an updraft allowing for high velocity speed.", AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public boolean executeStart() {
        return isCooldownReady();
    }

    @Override
    public boolean executeEnd() {
        resetCooldown();
        return true;
    }

    @Override
    public void onTickActive() {
        handleStamina();
        fly();

        if (!hasStamina(PASSIVE_STAMINA_USE))
            setActive(false);
    }

    @Override
    public void onTickInactive() {}

    private void fly() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();

        if (entity.isSprinting()) {
            entity.setVelocity(entity.getRotationVector());
            entity.velocityModified = true;

            serverWorld.spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 1, entity.getZ(), 4, 0.4, 0.5, 0.4, 0.1);
            serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, entity.getX(), entity.getY() + 1, entity.getZ(), 3, 0.4, 0.5, 0.4, 0.1);

            AffectAll.withinRadius(LivingEntity.class, entity.getWorld(), entity.getPos(), 3, 1, 3).exclude(entity).withVelocity(entity.getVelocity(), true);

        } else {
            if (entity instanceof ServerPlayerEntity player)
                sendDescentPacket(player);
            else
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 2, 2, true, false));

            serverWorld.spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 10, 1, 0, 1, 0.1);
        }
    }

    private void sendDescentPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, Networking.S2C_WIND_FLY_DESCENT_VELOCITY, PacketByteBufs.empty());
    }

    private void handleStamina() {
        if(getUser().getStamina() >= PASSIVE_STAMINA_USE) {
            getUser().setStamina(getUser().getStamina() - PASSIVE_STAMINA_USE);
        }
    }
}
