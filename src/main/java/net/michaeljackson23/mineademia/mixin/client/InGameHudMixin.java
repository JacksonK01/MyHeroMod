package net.michaeljackson23.mineademia.mixin.client;

import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Shadow @Final private static Identifier POWDER_SNOW_OUTLINE;

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        var player = MinecraftClient.getInstance().player;
        if(player.hasStatusEffect(StatusEffectsRegister.EFFECT_FROZEN)){
            renderOverlay(context, POWDER_SNOW_OUTLINE, 0f);
        }
    }
}
