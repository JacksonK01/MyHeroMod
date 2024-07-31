package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.quirk.quirks.*;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.savedata.QuirkBuilder;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.datastructures.IndexMap;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Random;

/**
 * <p>
 * This is where everything starts. It holds a map of all the quirks and
 * the event for when the player joins
 * </p>
 * <p>
 *     After reading this, check out {@link net.michaeljackson23.mineademia.savedata.StateSaverAndLoader} and {@link net.michaeljackson23.mineademia.quirk.ServerQuirkTicks}
 * </p>
 */
public class QuirkInitialize {
    /**
     * An IndexMap is a custom data structure used specifically for
     * what we need here, which is to store the index along with the values
     * in the map. For this instance of the IndexMap, I store a string as the id,
     * and the constructor for the quirk, this is so everytime a new player joins
     * the game, a new instance in memory of whatever quirk they get is created.
     * It allows the player to have a version of the quirk that is unique to them
     */
    private static final IndexMap<String, QuirkConstructor<Quirk>> allQuirks = new IndexMap<>();
    static {
        allQuirks.put("One For All", OneForAll::new);
        allQuirks.put("Explosion", Explosion::new);
        allQuirks.put("Half-Cold Half-Hot", HalfColdHalfHot::new);
        allQuirks.put("Whirlwind", Whirlwind::new);
        allQuirks.put("Electrification", Electrification::new);
        allQuirks.put("Engine", Engine::new);
        allQuirks.put("Rifle", Rifle::new);
    }
    private static final Random rand = new Random();
    /**
     * <p>
     *      Hooking = Adding code into a specific spot. Hooking is used as a term to describe a spot developers
     *      left open for programmers to add their custom code. All of minecraft modding uses hooks to insert custom code into the game.
     * </p>
     * <p>
     *      This method is creating a custom event which will be hooked into the
     *      method that gets called when the player joins the game. AKA this code
     *      will be run when the player enters a world/server. This next part might look weird
     *      but the way I have set it up (Which imo isn't good) it'll read what data it has stored
     *      on the player in {@link net.michaeljackson23.mineademia.savedata.StateSaverAndLoader} and set a pointer stored on the player to that data.
     *      All this method does is set up the player and reads what data has been stored
     * </p>
     *
     * <p>
     *     Used by = {@link Mineademia#onInitialize()}
     * </p>
     */
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
    /**
     * <p>
     *     Selects a random quirk
     * </p>
     *
     * <p>
     *     Used by = {@link QuirkInitialize#InitializeEvent()}
     * </p>
     */
    public static Quirk getRandomQuirk() {
        int randomInt = rand.nextInt(allQuirks.getSize());
        return allQuirks.getValue(randomInt).construct();
    }
    /**
     * <p>
     *     Uses a string to get a quirk
     * </p>
     *
     * <p>
     *     Used by = {@link QuirkInitialize#InitializeEvent()}, {@link net.michaeljackson23.mineademia.networking.ServerPackets#openQuirkTabletGUI(MinecraftServer, ServerPlayerEntity, ServerPlayNetworkHandler, PacketByteBuf, PacketSender)}
     * </p>
     */
    public static Quirk setQuirkWithString(String quirk) {
        return allQuirks.getValue(quirk).construct();
    }

    /**
     * This interface returns T which can be any datatype. This is
     * called a generic. It's only used for the index map.
     */
    private interface QuirkConstructor<T> {
        T construct();
    }
}

