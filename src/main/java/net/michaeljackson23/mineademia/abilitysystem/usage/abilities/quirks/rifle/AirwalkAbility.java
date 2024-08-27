package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class AirwalkAbility extends ToggleAbility {

    public static final String DESCRIPTION = "";

    public static final int STAMINA_COST_HOP = 0; // 10
    public static final int STAMINA_COST_FLOAT = 0; // 2

    public static final float HOP_RADIUS = 0.5f;


    public AirwalkAbility(@NotNull IAbilityUser user) {
        super(user, "Air Walk", DESCRIPTION, Networking.C2S_ABILITY_TWO, AbilityCategory.UTILITY, AbilityCategory.MOBILITY);
    }

    @Override
    public boolean executeStart() {
        if (getEntity() instanceof ServerPlayerEntity player)
            player.sendMessage(Text.literal("Airwalk: ON"), true);

        return true;
    }

    @Override
    public boolean executeEnd() {
        if (getEntity() instanceof ServerPlayerEntity player)
            player.sendMessage(Text.literal("Airwalk: OFF"), true);

        return true;
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        entity.fallDistance = 0;
        if (!entity.isOnGround() && entity.isSprinting() && getTicks() % 10 == 0 && hasStaminaAndConsume(STAMINA_COST_HOP)) {
            Vec3d forward = entity.getRotationVecClient().normalize().add(Mathf.Vector.UP.multiply(0.25f)).multiply(0.75f);

            entity.setVelocity(forward);
            entity.velocityModified = true;

            DrawParticles.forWorld(world).inCircle(entity.getPos(), Mathf.Vector.UP, HOP_RADIUS, 5, ParticleTypes.CLOUD, false);
        } else if (!entity.isOnGround() &&!entity.isSprinting() && hasStaminaAndConsume(STAMINA_COST_FLOAT)) {
            if (entity instanceof ServerPlayerEntity player) {
                Vec3d velocity = player.getVelocity();

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeVec3d(new Vec3d(Double.MAX_VALUE, 0, Double.MAX_VALUE));

                ServerPlayNetworking.send(player, Networking.S2C_SET_VELOCITY, buffer);
            } else
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 254, false, false, false));
        }
    }

}
