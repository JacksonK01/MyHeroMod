package net.michaeljackson23.mineademia.mixin.common;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.callbacks.OnPlayerRespawnCallback;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(at = @At("TAIL"), method = "respawnPlayer")
    private void respawnPlayer(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ActionResult result = OnPlayerRespawnCallback.EVENT.invoker().onRespawn(cir.getReturnValue());

        PlayerAbilityUser user = (PlayerAbilityUser) AbilityManager.getPlayerUser(oldPlayer);
        if (user != null)
            user.setEntity(cir.getReturnValue());

        if(result == ActionResult.FAIL) {
            cir.cancel();
        }
    }
}
