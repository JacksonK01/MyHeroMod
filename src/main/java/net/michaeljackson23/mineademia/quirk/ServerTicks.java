package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.network.PacketByteBuf;

public class ServerTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    public static void AbilitiesTickEvent() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                playerState.getQuirk().tick(player);

                Quirk quirkPointer = playerState.getQuirk();
                PacketByteBuf dataHud = PacketByteBufs.create();
                dataHud.writeString(quirkPointer.getName());
                dataHud.writeDouble(quirkPointer.getStamina());
                dataHud.writeInt(quirkPointer.getCooldown());
                ServerPlayNetworking.send(player, Networking.QUIRK_DATA, dataHud);
            });
        });
    }
}
