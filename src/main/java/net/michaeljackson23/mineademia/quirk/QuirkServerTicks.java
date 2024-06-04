package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;

public class QuirkServerTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    public static void serverTickRegister() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                if(!(player instanceof QuirkDataHelper quirkPlayer)) {
                    return;
                }
                quirkPlayer.myHeroMod$getQuirk(server).tick(player);
                QuirkDataPacket.send(player);
            });
        });
    }
}
