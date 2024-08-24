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

    public static final int STAMINA_COST_HOP = 10;
    public static final int STAMINA_COST_FLOAT = 2;

    public static final float HOP_RADIUS = 0.5f;


    public AirwalkAbility(@NotNull IAbilityUser user) {
        super(user, "Air Walk", DESCRIPTION, Networking.C2S_ABILITY_TWO, AbilityCategory.UTILITY, AbilityCategory.MOBILITY);
    }

    @Override
    public boolean executeStart() {
        getEntity().sendMessage(Text.literal("ON"));
        return true;
    }

    @Override
    public boolean executeEnd() {
        getEntity().sendMessage(Text.literal("OFF"));
        return true;
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (!entity.isOnGround() && entity.isSprinting() && getTicks() % 10 == 0 && hasStaminaAndConsume(STAMINA_COST_HOP)) {
            Vec3d forward = entity.getRotationVecClient().normalize().add(Mathf.Vector.UP.multiply(0.25f)).multiply(0.75f);

            entity.setVelocity(forward);
            entity.velocityModified = true;

            DrawParticles.forWorld(world).inCircle(entity.getPos(), Mathf.Vector.UP, HOP_RADIUS, 5, ParticleTypes.CLOUD, false);

            entity.fallDistance = 0;
        } else if (!entity.isOnGround() &&!entity.isSprinting() && hasStaminaAndConsume(STAMINA_COST_FLOAT)) {
            entity.fallDistance = 0;

            if (entity instanceof ServerPlayerEntity player) {
                Vec3d velocity = player.getVelocity();

                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeVec3d(new Vec3d(velocity.x, 0, velocity.z));

                ServerPlayNetworking.send(player, Networking.S2C_SET_VELOCITY, buffer);
            } else
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 254, false, false, false));
        }
    }

}
