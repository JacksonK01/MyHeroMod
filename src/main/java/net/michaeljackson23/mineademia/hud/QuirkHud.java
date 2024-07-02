package net.michaeljackson23.mineademia.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class QuirkHud {
    private static final Identifier STAMINA_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_full.png");
    private static final Identifier STAMINA_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_empty.png");

    private static final Identifier COOLDOWN_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_empty.png");
    private static final Identifier COOLDOWN_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_full.png");

    private static final int STAMINA_WIDTH = 90;
    private static final int STAMINA_HEIGHT = 15;

    private static final int MAX_STAMINA = 1000;

    private static final int COOLDOWN_WIDTH = 90;
    private static final int COOLDOWN_HEIGHT = 15;

    private static final int MAX_COOLDOWN = 30 * 10;

    private static boolean shouldFlash = false;
    private static boolean visible = true;

    public static void register() {
        HudRenderCallback.EVENT.register((context, tick) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client != null) {
                if(!(client.player instanceof QuirkDataAccessors quirkPlayer)) {
                    return;
                }
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                int x = width/5;
                int y = height/5;

                int stamina_X = 10;
                int stamina_Y = height - STAMINA_HEIGHT - 5;

                context.drawTexture(STAMINA_EMPTY, stamina_X, stamina_Y, 0, 0, STAMINA_WIDTH, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
                context.drawTexture(STAMINA_FULL, stamina_X, stamina_Y, 0, 0, (int) (quirkPlayer.myHeroMod$getQuirkData().getStamina() / MAX_STAMINA * STAMINA_WIDTH), STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);

                int cooldown_X = stamina_X;
                int cooldown_Y = stamina_Y - COOLDOWN_HEIGHT - 5;

                context.drawTexture(COOLDOWN_EMPTY, cooldown_X, cooldown_Y, 0, 0, COOLDOWN_WIDTH, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);


                int currentCooldown = quirkPlayer.myHeroMod$getQuirkData().getCooldown() * 10;

                shouldFlash = currentCooldown > MAX_COOLDOWN;

                int scaledCooldownWidth = (int) ((double) Math.min(currentCooldown, MAX_COOLDOWN) / MAX_COOLDOWN * COOLDOWN_WIDTH);

                if(shouldFlash) {
                    if(client.player.age % 10 < 5) {
                        scaledCooldownWidth = 0;
                    }
                }

                context.drawTexture(COOLDOWN_FULL, cooldown_X, cooldown_Y, 0, 0, scaledCooldownWidth, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);

                TextRenderer textRenderer = client.textRenderer;
                context.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Quirk: " + quirkPlayer.myHeroMod$getQuirkData().getQuirkName()),
                        x, y, 0xff0000);
                context.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Stamina: " + quirkPlayer.myHeroMod$getQuirkData().getStamina()),
                        x, y + 20, 0xff0000);
                context.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Cooldown: " + quirkPlayer.myHeroMod$getQuirkData().getCooldown()),
                        x, y + 40, 0xff0000);
            }
        });
    }
}


