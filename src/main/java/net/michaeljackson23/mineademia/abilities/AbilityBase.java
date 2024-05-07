package net.michaeljackson23.mineademia.abilities;

import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbilityBase {
    public ServerPlayerEntity player;
    public PlayerData playerData;
    public MinecraftServer server;
    public int timer = 0;
    public int slot;

    public AbilityBase(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
        this.player = player;
        this.playerData = playerData;
        this.server = server;
        this.slot = slot;
    }

    public abstract void activate();

    public abstract void deactivate();
}
