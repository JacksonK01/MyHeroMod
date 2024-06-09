package net.michaeljackson23.mineademia.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
/**
 * Callback for initializing a ServerPlayerEntity Object
 * Called at the end of the constructor for ServerPlayerEntities.
 */
public interface OnServerPlayerCreationCallback {
    Event<OnServerPlayerCreationCallback> EVENT = EventFactory.createArrayBacked(OnServerPlayerCreationCallback.class,
            (listeners) -> (player) -> {
                for(OnServerPlayerCreationCallback listener : listeners) {
                    ActionResult result = listener.syncPlayerData(player);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

     ActionResult syncPlayerData(ServerPlayerEntity player);
}
