package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.Iterator;
import java.util.Queue;

import static net.michaeljackson23.mineademia.hud.DevHudElements.DEV_HUD_SYNC;

public class AbilitiesTicks {
    //This where the server event checks if the player has an ability active, and whether they need to regenerate stamina, and whether to decrease the cooldown
    //TODO fix this mess
    public static void AbilitiesTickEvent() {
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

                    Iterator<PassiveAbility> passiveAbilityIterator = playerState.getPassiveAbilities().iterator();
                    while (passiveAbilityIterator.hasNext()) {
                        PassiveAbility next = passiveAbilityIterator.next();
                        if(next.activate()) {
                            passiveAbilityIterator.remove();
                            player.sendMessage(Text.literal("Removed Passive"));
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
                });
            });
        });
    }
}
