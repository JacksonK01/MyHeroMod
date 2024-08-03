package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class WindFlyAbility extends ToggleAbility implements IStaminaAbility, ICooldownAbility {
    private final Cooldown cooldown;
    private static final int PASSIVE_STAMINA_USE = 1;

    public WindFlyAbility(@NotNull IAbilityUser user) {
        super(user, "Fly", "User's wind creates an updraft allowing for high velocity speed.", AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(5);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 0;
    }

    @Override
    public boolean executeStart() {
        getEntity().sendMessage(Text.literal("executeStart"));
        return isReady();
    }

    @Override
    public void executeEnd() {
        getEntity().sendMessage(Text.literal("executeEnd"));
        reset();
    }

    @Override
    public boolean onTickActive() {
        handleStamina();
        fly();
        getEntity().sendMessage(Text.literal("onTickActive"));
        return getUser().getStamina() >= PASSIVE_STAMINA_USE;
    }

    @Override
    public void onTickInactive() {}

    private void fly() {
        if(getEntity().isSprinting()) {
            getEntity().setVelocity(getEntity().getRotationVector());
            getEntity().velocityModified = true;
            ServerWorld serverWorld = (ServerWorld) getEntity().getWorld();
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    getEntity().getX(), getEntity().getY() + 1, getEntity().getZ(),
                    4,
                    0.4, 0.5, 0.4,
                    0.1);
            serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK,
                    getEntity().getX(), getEntity().getY() + 1, getEntity().getZ(),
                    3,
                    0.4, 0.5, 0.4,
                    0.1);
            AreaOfEffect.execute(getEntity(), 3, 1, getEntity().getX(), getEntity().getY(), getEntity().getZ(), (entityToAffect -> {
                entityToAffect.setVelocity(getEntity().getVelocity());
                entityToAffect.velocityModified = true;
            }));
        } else {
            if(getEntity() instanceof ServerPlayerEntity player) {
                sendDescentPacket(player);
            } else {
                getEntity().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 2, 2, true, false));
            }
            ServerWorld serverWorld = (ServerWorld) getEntity().getWorld();
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    getEntity().getX(), getEntity().getY(), getEntity().getZ(),
                    10,
                    1, 0, 1,
                    0.1);
        }
    }

    private void sendDescentPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, Networking.WIND_FLY_DESCENT_VELOCITY, PacketByteBufs.empty());
    }

    private void handleStamina() {
        if(getUser().getStamina() >= PASSIVE_STAMINA_USE) {
            getUser().setStamina(getUser().getStamina() - PASSIVE_STAMINA_USE);
        }
    }
}
