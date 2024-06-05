package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;

public class ServerQuirkTicks {

    public static void serverTickRegister() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                if(!(player instanceof QuirkDataHelper quirkPlayer)) {
                    return;
                }
                Quirk quirk = quirkPlayer.myHeroMod$getQuirk(server);
                //The tick method sends a packet to the client using the ability as well for a polish sync
                quirk.tick(player);
                quirkPlayer.myHeroMod$getQuirkData().syncStaminaAndCooldown(quirk);
                if(quirk.tickCounter <= 0) {
                    QuirkDataPacket.sendProxy(player);
                    quirk.tickCounter = 40;
                }
                quirk.tickCounter--;
            });
        });
    }
}
