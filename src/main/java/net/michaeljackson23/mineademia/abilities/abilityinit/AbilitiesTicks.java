package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.AbilityMap;
import net.michaeljackson23.mineademia.hud.DevQuirkDisplay;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.Queue;

import static net.michaeljackson23.mineademia.hud.DevHudElements.DEV_HUD_SYNC;

public class AbilitiesTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    //TODO fix this mess
    public static void AbilitiesTickEvent() {
        PacketByteBuf data = PacketByteBufs.create();
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.execute(() -> {
                server.getPlayerManager().getPlayerList().forEach((player) -> {
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    Queue<AbilityBase> abilityQueuePointer = playerState.getAbilityQueue();
                    if (abilityQueuePointer.peek() != null) {
                        abilityQueuePointer.peek().execute(player, playerState, server);
                        if(!abilityQueuePointer.peek().isActive()) {
                            abilityQueuePointer.remove();
                        }
                    }

                    if (playerState.getStamina() < 1000) {
                        playerState.setStamina(playerState.getStamina() + 1);
                    }

                    if (playerState.getCooldown() > 0) {
                        playerState.setCooldown(playerState.getCooldown() - 1);
                    }

                    MinecraftClient client = MinecraftClient.getInstance();
                    if(client != null && client.isFinishedLoading()) {
                        PacketByteBuf dataHud = PacketByteBufs.create();
                        dataHud.writeString(playerState.getQuirk());
                        dataHud.writeInt(playerState.getStamina());
                        dataHud.writeInt(playerState.getCooldown());
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
