package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.minecraft.text.Text;

public class ServerQuirkTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    //Every 40 ticks it sends your quirkdata to every client
    //Every 20 ticks it sends your quirdata to only your client
    private static int tickCounter = 0;
    public static void serverTickRegister() {
        //To prevent un needed packets being sent often
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                if(!(player instanceof QuirkDataHelper quirkPlayer)) {
                    return;
                }
                if(tickCounter == 0) {
                    QuirkDataPacket.sendToAll(player);
                    tickCounter = 40;
                } else {
                    if(tickCounter == 20) {
                        QuirkDataPacket.send(player);
                    }
                }
                tickCounter--;
                quirkPlayer.myHeroMod$getQuirk(server).tick(player);
            });
        });
    }
}
