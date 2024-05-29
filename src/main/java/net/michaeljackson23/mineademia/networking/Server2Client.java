package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.hud.DevQuirkDisplay;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Server2Client {
    //This class is when the client needs to listen for a packet sent by the server
    public static final Identifier INITIAL_SYNC = new Identifier(Mineademia.Mod_id, "initial_sync");
    public static final Identifier QUIRK_TABLET_GUI = new Identifier(Mineademia.Mod_id, "quirk_tablet_gui");

    public static void registerServerToClientPackets() {
        Mineademia.LOGGER.info("Registering all Server to Client event listener");
        ClientPlayNetworking.registerGlobalReceiver(QUIRK_TABLET_GUI, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                QuirkTabletGui quirkTabletGui = new QuirkTabletGui(Text.literal("Quirk Tablet"));
                quirkTabletGui.init(client, 50, 50);
                quirkTabletGui.shouldPause();
                client.setScreenAndRender(quirkTabletGui);
            });
        });
    }
}
