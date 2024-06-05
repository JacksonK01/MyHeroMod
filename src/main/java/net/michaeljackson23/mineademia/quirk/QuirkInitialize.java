package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.quirk.quirks.Electrification;
import net.michaeljackson23.mineademia.quirk.quirks.Explosion;
import net.michaeljackson23.mineademia.quirk.quirks.HalfColdHalfHot;
import net.michaeljackson23.mineademia.quirk.quirks.OneForAll;
import net.michaeljackson23.mineademia.quirk.quirks.Whirlwind;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.IndexMap;
import net.minecraft.server.MinecraftServer;
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
            if(!(player instanceof QuirkDataHelper quirkPlayer)) {
                return;
            }
            QuirkData quirkData = quirkPlayer.myHeroMod$getQuirkData();
            String storedName = quirkData.getQuirkName();
            buildQuirk(player, quirkPlayer.myHeroMod$getQuirkData().getQuirkName());
            if(storedName.isEmpty()) {
                player.sendMessage(Text.literal("Set quirk to " + quirkData.getQuirkName()));
            } else {
                player.sendMessage(Text.literal("Picked up " + quirkData.getQuirkName() + " quirk"));
            }
            AnimationProxy.sendStopAnimation(player);
        }));
    }

    public static void buildQuirk(ServerPlayerEntity player, String quirk) {
        if(!(player instanceof QuirkDataHelper quirkPlayer)) {
            return;
        }
        if(quirk.isEmpty()) {
            quirkRandom(player.getServer(), quirkPlayer);
        } else {
            setQuirkWithString(player.getServer(), quirkPlayer, quirk);
        }
        quirkPlayer.myHeroMod$getQuirkData().buildFromQuirk(quirkPlayer.myHeroMod$getQuirk(player.getServer()));
    }

    private static void quirkRandom(MinecraftServer server, QuirkDataHelper player) {
        int randomInt = rand.nextInt(allQuirks.getSize());
        player.myHeroMod$setQuirk(server, allQuirks.getValue(randomInt).construct());
    }

    private static void setQuirkWithString(MinecraftServer server, QuirkDataHelper player, String quirk) {
        Quirk quirkToSet = allQuirks.getValue(quirk).construct();
        player.myHeroMod$setQuirk(server, quirkToSet);
    }

    private interface QuirkConstructor<T> {
        T construct();
    }
}

