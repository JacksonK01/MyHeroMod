package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BooleanSupplier;

/**
 * <p>
 *     Every second the server calls the {@link net.minecraft.server.MinecraftServer#tick(BooleanSupplier)} method
 *     20 times. The tick method is what runs the game and keeps it moving. 20 ticks = 1 second. In this event
 *     that is registered, we are getting the player's quirk and calling {@link Quirk#tick(ServerPlayerEntity)} method.
 *     If you haven't yet, check out {@link net.michaeljackson23.mineademia.savedata.StateSaverAndLoader} and {@link net.michaeljackson23.mineademia.savedata.PlayerData}
 * </p>
 */
public class ServerQuirkTicks {
    public static void serverTickRegister() {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                Quirk quirk = ((QuirkAccessor) player).myHeroMod$getQuirk();
                //The tick method sends a packet to the client using the ability as well for a polish sync
                quirk.tick(player);
                if(quirk.tickCounter <= 0) {
                    //Ignore this for now
                    QuirkDataPacket.sendProxy(player);
                    quirk.tickCounter = 40;
                }
                quirk.tickCounter--;
            });
        });
    }
}
