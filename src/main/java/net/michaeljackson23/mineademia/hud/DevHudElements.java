package net.michaeljackson23.mineademia.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.michaeljackson23.mineademia.networking.Client2Server.*;

public class DevHudElements {
    public static String quirk;
    public static int stamina;
    public static int cooldown;

    public static final Identifier DEV_HUD_SYNC = new Identifier(Mineademia.Mod_id, "dev_hud_sync");

    public static void UpdateHudElements() {
        ClientPlayNetworking.registerGlobalReceiver(DEV_HUD_SYNC, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                quirk = buf.readString();
                stamina = buf.readInt();
                cooldown = buf.readInt();
            });
        });
    }
}
