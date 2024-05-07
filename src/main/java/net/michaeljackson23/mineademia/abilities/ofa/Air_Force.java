package net.michaeljackson23.mineademia.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.IAbilityHandler;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.michaeljackson23.mineademia.hud.DevHudElements.DEV_HUD_SYNC;

public class Air_Force extends AbilityBase {
    public static final String title = "Air Force";
    public static final String description = "The user flicks their fingers and creates intense wind pressure";
    private final int staminaDrain = 50;
    private final int cooldownAdd = 50;

    public Air_Force(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
        super(player, playerData, server, slot);
    }

    @Override
    public void activate() {
        playerData.quirkStamina = playerData.quirkStamina - staminaDrain;
        playerData.quirkCooldown = cooldownAdd;
        player.addVelocity(PlayerAngleVector.getPlayerAngleVector(player, -0.5, -0.5));
        player.velocityModified = true;

        AirForceProjectile airForceProjectile = new AirForceProjectile(player.getWorld(), player);
        airForceProjectile.setVelocity(player, player.getPitch(), player.getYaw(), 0f, 2f, 0);
        player.getWorld().spawnEntity(airForceProjectile);

        player.swingHand(player.getActiveHand(), true);

        playerData.quirkAbilityTimers[slot]++;
        if(playerData.quirkAbilityTimers[slot] >= 1) {
            playerData.quirkAbilityTimers[slot] = 0;
            playerData.abilityActive[slot] = false;
            playerData.abilityStack.pop();
        }
    }

    @Override
    public void deactivate() {

    }
}
