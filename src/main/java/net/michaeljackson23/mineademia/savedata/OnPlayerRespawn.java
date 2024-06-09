package net.michaeljackson23.mineademia.savedata;

import net.michaeljackson23.mineademia.callbacks.OnPlayerRespawnCallback;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class OnPlayerRespawn {
    public static void register() {
        OnPlayerRespawnCallback.EVENT.register((player -> {
            PlayerData data = StateSaverAndLoader.getPlayerState(player);
            ((PlayerDataAccessor) player).myHeroMod$setPlayerData(data);
            QuirkDataPacket.send(player);
            return ActionResult.SUCCESS;
        }));
    }
}
