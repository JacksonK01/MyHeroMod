package net.michaeljackson23.mineademia.client.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.networking.PlayerAbilityUserPacketS2C;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Random;

@Environment(EnvType.CLIENT)
public class QuirkHud {
    //Toggles whether all cooldowns are shown on screen at once
    public static boolean showAllCooldowns = true;

    private static final Identifier STAMINA_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_full.png");
    private static final Identifier STAMINA_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_empty.png");

    private static final Identifier COOLDOWN_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_empty.png");
    private static final Identifier COOLDOWN_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_full.png");

    private static final int STAMINA_WIDTH = 80;
    private static final int STAMINA_HEIGHT = 8;

    private static final int MAX_STAMINA = 1000;

    private static final int COOLDOWN_WIDTH = 80;
    private static final int COOLDOWN_HEIGHT = 8;

    private static final int MAX_COOLDOWN = 30 * 10;

    private static boolean shouldFlash = false;

    private static int SPACING = 5;

    //TODO delete!!
    static int tempCounter = 0;

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
            }
        });
    }

    public static void display(DrawContext context, float tick) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerAbilityUserPacketS2C abilityData = ClientCache.get();

        if(abilityData == null) {
            return;
        }

        int height = client.getWindow().getScaledHeight();

        int globalX = 10;

        //This variable keeps track of where the y is for cooldown and stamina
        int globalY = height - STAMINA_HEIGHT - SPACING;


        context.drawTexture(STAMINA_EMPTY, globalX, globalY, 0, 0, STAMINA_WIDTH, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
        context.drawTexture(STAMINA_FULL, globalX, globalY, 0, 0, (abilityData.getStamina() / abilityData.getMaxStamina() * STAMINA_WIDTH), STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);

        int[] cooldowns = abilityData.getCooldowns();
        int[] maxCooldowns = abilityData.getMaxCooldowns();

        int yIncrement = COOLDOWN_HEIGHT + SPACING;

        for(int i = 0; i < cooldowns.length; i++) {
            if(showAllCooldowns || cooldowns[i] > 0) {
                globalY -= yIncrement;

                context.drawTexture(COOLDOWN_EMPTY, globalX, globalY, 0, 0, COOLDOWN_WIDTH, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);

                int scaledCooldownWidth = (int) ((double) Math.min(cooldowns[i], maxCooldowns[i]) / maxCooldowns[i] * COOLDOWN_WIDTH);

                context.drawTexture(COOLDOWN_FULL, globalX, globalY, 0, 0, scaledCooldownWidth, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);
            }
        }
    }
}


