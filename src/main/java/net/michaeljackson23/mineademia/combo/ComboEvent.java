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
            PlayerData playerData = ((PlayerDataAccessor) attacker).myHeroMod$getPlayerData();
            playerData.getComboManager().notifyPunch(target);
            attacker.sendMessage(Text.literal("[Attacker = " + attacker.getName().getString() + "] [Target = " + target.getName().getString() + "] Damage = " + damage.getData()));
            return ActionResult.SUCCESS;
        }));
    }
}
