package net.michaeljackson23.mineademia.abilities;


import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Empty extends AbilityBase {
    private Empty() {
        super(0, 0, 0, false, "empty", "no ability");
    }

    @Override
    public void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {

    }

    public static AbilityBase getInstance() {
        return new Empty();
    }
}
