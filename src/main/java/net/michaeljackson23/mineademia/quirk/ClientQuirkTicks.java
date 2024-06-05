package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;

import static net.michaeljackson23.mineademia.networking.Networking.*;
import static net.michaeljackson23.mineademia.networking.Networking.DODGE;

public class ClientQuirkTicks {
    public static void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player instanceof QuirkDataHelper quirkPlayer) {
                QuirkData quirkData = quirkPlayer.myHeroMod$getQuirkData();
                if(quirkData.getStamina() < 1000) {
                    quirkData.setStamina(quirkData.getStamina() + 1);
                } else {
                    quirkData.setStamina(1000);
                }

                if(quirkData.getCooldown() > 0) {
                    quirkData.setCooldown(quirkData.getCooldown() - 1);
                }
            }
        });
    }
}
