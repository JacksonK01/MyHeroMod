package net.michaeljackson23.mineademia.combo;

import net.michaeljackson23.mineademia.callbacks.BeforeEntityDamageCallback;
import net.michaeljackson23.mineademia.callbacks.OnPlayerAttackEntity;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class ComboEvent {
    public static void register() {
        OnPlayerAttackEntity.EVENT.register(((attacker, target, damage) -> {
            if(attacker.getMainHandStack().isEmpty()) {
                PlayerData playerData = ((PlayerDataAccessor) attacker).myHeroMod$getPlayerData();
                playerData.getComboManager().notifyPunch(attacker, target);
            }

            attacker.sendMessage(Text.literal(target.getName().getString()));
            return ActionResult.SUCCESS;
        }));
    }
}
