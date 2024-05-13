package net.michaeljackson23.mineademia.abilities.explosion;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExplosionDash extends AbilityBase {
    private ExplosionDash(int abilityDuration, int staminaDrain, int cooldownAdd, boolean isLoop, String title, String description) {
        super(abilityDuration, staminaDrain, cooldownAdd, isLoop, title, description);
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {

    }
}
