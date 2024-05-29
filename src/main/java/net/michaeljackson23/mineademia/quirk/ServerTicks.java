package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Iterator;

import static net.michaeljackson23.mineademia.hud.DevHudElements.DEV_HUD_SYNC;

public class ServerTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    public static void AbilitiesTickEvent() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((player) -> {
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    playerState.getQuirk().tick(player);

                    MinecraftClient client = MinecraftClient.getInstance();
                    if(client != null && client.isFinishedLoading()) {
                        Quirk quirkPointer = playerState.getQuirk();
                        PacketByteBuf dataHud = PacketByteBufs.create();
                        dataHud.writeString(quirkPointer.getName());
                        dataHud.writeDouble(quirkPointer.getStamina());
                        dataHud.writeInt(quirkPointer.getCooldown());
                        ServerPlayNetworking.send(player, DEV_HUD_SYNC, dataHud);
                    }
                });
            });
        });
    }
}
