package net.michaeljackson23.mineademia.init;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.QuirkAbilities;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Random;

import static net.michaeljackson23.mineademia.networking.Server2Client.CHANGED_QUIRK;
import static net.michaeljackson23.mineademia.networking.Server2Client.INITIAL_SYNC;

public class QuirkInitialize {
    private static List<String> allQuirks = List.of("One For All", "Explosion", "Half-Cold Half-Hot", "Whirlwind", "Electrification");

    public static void InitializeEvent() {
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            handler.getPlayer().sendMessage(Text.literal("Player join world event is working!!"));
            PlayerData playerData = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();

            if(playerData.playerQuirk.equals("empty")) {
                playerData = quirkSetup(playerData, handler.getPlayer());
            }

            data.writeString(playerData.playerQuirk);
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        }));
    }

    public static int[] abilitySetup(String quirk) {
        int[] abilities = new int[5];

        for(int i = 0;i < abilities.length;i++) {
            abilities[i] = 0;
        }

        if(quirk.equals("One For All")) {
            abilities[0] = QuirkAbilities.AIR_FORCE.getValue();
            abilities[1] = QuirkAbilities.BLACKWHIP.getValue();
            abilities[4] = QuirkAbilities.COWLING.getValue();
        }
        return abilities;
    }

    public static PlayerData quirkSetup(PlayerData playerData, ServerPlayerEntity player) {
        Random rand = new Random();
        int randomInt = rand.nextInt(allQuirks.size());
        playerData.playerQuirk = allQuirks.get(randomInt);
        playerData.quirkAbilities = abilitySetup(playerData.playerQuirk);
        return playerData;
    }

    public static PlayerData setQuirk(PlayerData playerData, String quirk) {
        playerData.playerQuirk = quirk;
        playerData.quirkAbilities = abilitySetup(quirk);
        return playerData;
    }
}

