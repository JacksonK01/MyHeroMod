package net.michaeljackson23.mineademia.quirk.events;

import net.michaeljackson23.mineademia.callbacks.BeforeEntityDamageCallback;
import net.minecraft.util.ActionResult;

public class BeforeDamageEvent {
    public static void register() {
        BeforeEntityDamageCallback.EVENT.register((entityAttacked, source, amount) -> {
//            if(entityAttacked instanceof ServerPlayerEntity player) {
//                player.sendMessage(Text.literal("Ow that hurt!"));
//                amount.setData(100f);
//            }
            return ActionResult.SUCCESS;
        });
    }
}
