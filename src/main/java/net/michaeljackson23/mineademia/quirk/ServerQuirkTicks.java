package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.michaeljackson23.mineademia.util.QuirkAccessor;

public class ServerQuirkTicks {

    public static void serverTickRegister() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                Quirk quirk = ((QuirkAccessor) player).myHeroMod$getQuirk();
                //The tick method sends a packet to the client using the ability as well for a polish sync
                quirk.tick(player);
                if(quirk.tickCounter <= 0) {
                    QuirkDataPacket.sendProxy(player);
                    quirk.tickCounter = 40;
                }
                quirk.tickCounter--;
            });
        });
    }
}
