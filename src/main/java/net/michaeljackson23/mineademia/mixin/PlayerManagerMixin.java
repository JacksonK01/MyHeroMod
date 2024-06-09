package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.callbacks.OnPlayerRespawnCallback;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(at = @At("TAIL"), method = "respawnPlayer")
    private void respawnPlayer(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ActionResult result = OnPlayerRespawnCallback.EVENT.invoker().onRespawn(cir.getReturnValue());

        if(result == ActionResult.FAIL) {
            cir.cancel();
        }
    }
}
