package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.michaeljackson23.mineademia.quirk.quirks.Electrification;
import net.michaeljackson23.mineademia.quirk.quirks.Explosion;
import net.michaeljackson23.mineademia.quirk.quirks.HalfColdHalfHot;
import net.michaeljackson23.mineademia.quirk.quirks.OneForAll;
import net.michaeljackson23.mineademia.quirk.quirks.Whirlwind;
import net.michaeljackson23.mineademia.util.IndexMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Random;

public class QuirkInitialize {
    private static final IndexMap<String, QuirkConstructor<Quirk>> allQuirks = new IndexMap<>();
    static {
        allQuirks.put("One For All", OneForAll::new);
        allQuirks.put("Explosion", Explosion::new);
        allQuirks.put("Half-Cold Half-Hot", HalfColdHalfHot::new);
        allQuirks.put("Whirlwind", Whirlwind::new);
        allQuirks.put("Electrification", Electrification::new);
    }
    private static final Random rand = new Random();

    public static void InitializeEvent() {
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            PlayerData playerData = StateSaverAndLoader.getPlayerState(player);

            if(playerData.getQuirk() == null) {
                quirkRandom(playerData);
                player.sendMessage(Text.literal("Set quirk to " + playerData.getQuirk().getName()));
            } else {
                player.sendMessage(Text.literal("Picked up " + playerData.getQuirk().getName() + " quirk"));
            }

        }));
    }

    public static void quirkRandom(PlayerData playerData) {
        int randomInt = rand.nextInt(allQuirks.getSize());
        playerData.setQuirk(allQuirks.getValue(randomInt).construct());
    }

    public static void setQuirkWithString(PlayerData playerData, String quirk) {
        Quirk quirkToSet = allQuirks.getValue(quirk).construct();
        playerData.setQuirk(quirkToSet);
    }


    private interface QuirkConstructor<T> {
        T construct();
    }
}

