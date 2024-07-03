package net.michaeljackson23.mineademia.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.michaeljackson23.mineademia.util.MutableObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface OnPlayerAttackEntity {
    Event<OnPlayerAttackEntity> EVENT = EventFactory.createArrayBacked(OnPlayerAttackEntity.class,
            (listeners) -> (PlayerEntity attacker, LivingEntity target, MutableObject<Float> damage) -> {
                for(OnPlayerAttackEntity listener : listeners) {
                    ActionResult result = listener.onAttack(attacker, target, damage);
                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onAttack(PlayerEntity attacker, LivingEntity target, MutableObject<Float> damage);
}
