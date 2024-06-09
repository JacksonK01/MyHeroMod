package net.michaeljackson23.mineademia.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.michaeljackson23.mineademia.util.MutableObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface BeforeEntityDamageCallback {
    Event<BeforeEntityDamageCallback> EVENT = EventFactory.createArrayBacked(BeforeEntityDamageCallback.class,
            (listeners) -> (entityAttacked, source, amount) -> {
                for(BeforeEntityDamageCallback listener : listeners) {
                    ActionResult result = listener.onDamage(entityAttacked, source, amount);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onDamage(LivingEntity entityAttacked, DamageSource source, MutableObject<Float> amount);
}
