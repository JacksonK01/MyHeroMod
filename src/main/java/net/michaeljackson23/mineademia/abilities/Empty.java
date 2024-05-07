package net.michaeljackson23.mineademia.abilities;


import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Empty extends AbilityBase {
    public Empty(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
        super(player, playerData, server, slot);
    }
    //I just need this here for the map

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }
}
