package net.michaeljackson23.mineademia.callbacks;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface OnPlayerRespawnCallback {
    Event<OnPlayerRespawnCallback> EVENT = EventFactory.createArrayBacked(OnPlayerRespawnCallback.class,
            (listeners) -> (player) -> {
                for(OnPlayerRespawnCallback listener : listeners) {
                    ActionResult result = listener.onRespawn(player);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onRespawn(ServerPlayerEntity player);
}
