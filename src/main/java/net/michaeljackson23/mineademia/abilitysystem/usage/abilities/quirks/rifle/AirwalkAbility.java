package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class AirwalkAbility extends ToggleAbility {

    public static final String DESCRIPTION = "";


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

//        if (entity.isSprinting())
            getEntity().setOnGround(true);
//        else
//            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 2, false, false, false));
    }

    @Override
    public void onEndTick() {
        if (isActive()) {
            LivingEntity entity = getEntity();

            if (entity.isSprinting() && getTicks() % 10 == 0) {
                Vec3d forward = entity.getRotationVecClient().normalize().add(Mathf.Vector.UP.multiply(0.25f)).multiply(0.75f);

                entity.setVelocity(forward);
                entity.velocityModified = true;
            } else if (!entity.isSprinting()) {
//                entity.addVelocity(Mathf.Vector.UP.multiply(0.08f));
                entity.fallDistance = 0;

                if (entity instanceof ServerPlayerEntity player)
                    ServerPlayNetworking.send(player, Networking.S2C_WIND_FLY_DESCENT_VELOCITY, PacketByteBufs.empty());
                else
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20, 254, false, false, false));
            }

//            LivingEntity entity = getEntity();

//            if (entity.isSprinting()) {
//                entity.setOnGround(true);
//            } else
//                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 2, false, false, false));
        }
    }
}
