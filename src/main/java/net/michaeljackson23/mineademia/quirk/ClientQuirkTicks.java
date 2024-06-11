package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;

@Environment(value= EnvType.CLIENT)
public class ClientQuirkTicks {
    public static void registerClientTicks() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player instanceof QuirkDataAccessors quirkPlayer) {
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
