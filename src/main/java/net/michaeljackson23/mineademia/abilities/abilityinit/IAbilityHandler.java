package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface IAbilityHandler {
    void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot);
}
