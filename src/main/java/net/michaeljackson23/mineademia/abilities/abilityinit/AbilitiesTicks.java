package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.abilityinit.AbilityMap;
import net.michaeljackson23.mineademia.hud.DevQuirkDisplay;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import static net.michaeljackson23.mineademia.hud.DevHudElements.DEV_HUD_SYNC;

public class AbilitiesTicks {
    public static void AbilitiesTickEvent() {
        PacketByteBuf data = PacketByteBufs.create();
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((player) -> {
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    if (!playerState.abilityStack.empty()) {
                        playerState.abilityStack.peek().activate();
                    }

                    if (playerState.quirkStamina < 1000) {
                        playerState.quirkStamina++;
                    }

                    if (playerState.quirkCooldown > 0) {
                        playerState.quirkCooldown--;
                    }

                    MinecraftClient client = MinecraftClient.getInstance();
                    if(client != null && client.isFinishedLoading()) {
                        PacketByteBuf dataHud = PacketByteBufs.create();
                        dataHud.writeString(playerState.playerQuirk);
                        dataHud.writeInt(playerState.quirkStamina);
                        dataHud.writeInt(playerState.quirkCooldown);
                        ServerPlayNetworking.send(player, DEV_HUD_SYNC, dataHud);
                    }
                    //Sending this info to update the hud
//                DevQuirkDisplay.playerQuirk = playerState.playerQuirk;
//                DevQuirkDisplay.abilityOne = playerState.quirkAbilityTimers[0];
//                DevQuirkDisplay.abilityTwo = playerState.quirkAbilityTimers[1];

                });
            });
        });
    }
}
