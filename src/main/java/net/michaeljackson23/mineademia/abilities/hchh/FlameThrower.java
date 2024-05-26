package net.michaeljackson23.mineademia.abilities.hchh;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlameThrower extends AbilityBase {

    public FlameThrower() {
        super(100, 10, 10, false, "Flame Thrower", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {

    }
}
