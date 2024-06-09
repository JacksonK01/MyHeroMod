package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.quirk.quirks.*;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.savedata.QuirkBuilder;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.IndexMap;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.server.network.ServerPlayerEntity;

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
            ((PlayerDataAccessor) player).myHeroMod$setPlayerData(playerData);
            QuirkBuilder builder = playerData.getQuirkBuilder();
            Quirk quirk;
            if(builder.isRandom()) {
                quirk = getRandomQuirk();
            } else {
                quirk = setQuirkWithString(builder.getQuirkName());
            }
            ((QuirkAccessor) player).myHeroMod$setQuirk(quirk);
            QuirkDataPacket.sendProxy(player);
            AnimationProxy.sendStopAnimation(player);
        }));
    }

    public static Quirk getRandomQuirk() {
        int randomInt = rand.nextInt(allQuirks.getSize());
        return allQuirks.getValue(randomInt).construct();
    }

    public static Quirk setQuirkWithString(String quirk) {
        return allQuirks.getValue(quirk).construct();
    }

    private interface QuirkConstructor<T> {
        T construct();
    }
}

