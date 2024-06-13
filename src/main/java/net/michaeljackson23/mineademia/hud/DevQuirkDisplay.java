package net.michaeljackson23.mineademia.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class DevQuirkDisplay {
    public static void register() {
        HudRenderCallback.EVENT.register((draw, tick) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client != null) {
                if(!(client.player instanceof QuirkDataAccessors quirkPlayer)) {
                    return;
                }
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                int x = width/5;
                int y = height/5;

                TextRenderer textRenderer = client.textRenderer;
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Quirk: " + quirkPlayer.myHeroMod$getQuirkData().getQuirkName()),
                        x, y, 0xff0000);
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Stamina: " + quirkPlayer.myHeroMod$getQuirkData().getStamina()),
                        x, y + 20, 0xff0000);
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Cooldown: " + quirkPlayer.myHeroMod$getQuirkData().getCooldown()),
                        x, y + 40, 0xff0000);
            }
        });
    }
}


