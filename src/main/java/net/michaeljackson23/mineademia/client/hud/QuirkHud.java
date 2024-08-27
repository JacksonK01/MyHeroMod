package net.michaeljackson23.mineademia.client.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class QuirkHud {
    //Toggles whether all cooldowns are shown on screen at once
    public static boolean showAllCooldowns = false;

    private static final Identifier STAMINA_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_full.png");
    private static final Identifier STAMINA_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_empty.png");

    private static final Identifier COOLDOWN_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_empty.png");
    private static final Identifier COOLDOWN_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_full.png");

    private static final int STAMINA_WIDTH = 80;
    private static final int STAMINA_HEIGHT = 9;

    private static final int COOLDOWN_WIDTH = 80;
    private static final int COOLDOWN_HEIGHT = 9;

    private static final int SPACING = 3;

    public static void display(DrawContext context, float tick) {
        // we will add that later, fuck git
//        MinecraftClient client = MinecraftClient.getInstance();
//        PlayerAbilityUserPacketS2C abilityData = ClientCache.get();
//
//        if(abilityData == null) {
//            return;
//        }
//
//        TextRenderer textRenderer = client.textRenderer;
//        int height = client.getWindow().getScaledHeight();
//        int globalX = 10;
//        //This variable keeps track of where the y is for cooldown and stamina
//        int globalY = height - STAMINA_HEIGHT - SPACING;
//        int staminaWidth = (int) ((double) abilityData.getStamina() / abilityData.getMaxStamina() * STAMINA_WIDTH);
//
//        Text staminaText = Text.literal("Stamina: ");
//        int staminaTextLength = textRenderer.getWidth(staminaText);
//
//        context.drawText(textRenderer, staminaText, globalX, globalY, 0xffffff, true);
//        context.drawTexture(STAMINA_EMPTY, globalX + staminaTextLength, globalY, 0, 0, STAMINA_WIDTH, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
//        context.drawTexture(STAMINA_FULL, globalX + staminaTextLength, globalY, 0, 0, staminaWidth, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
//
//        int[] cooldowns = abilityData.getCooldowns();
//        int[] maxCooldowns = abilityData.getMaxCooldowns();
//
//        int yIncrement = COOLDOWN_HEIGHT + SPACING;
//
//        for(int i = 0; i < cooldowns.length; i++) {
//            if(showAllCooldowns || cooldowns[i] > 0) {
//                globalY -= yIncrement;
//
//                Text abilityName = Text.literal(abilityData.getAbilities()[i] + ": ");
//                int textLength = textRenderer.getWidth(abilityName);
//                int scaledCooldownWidth = (int) ((double) Math.min(cooldowns[i], maxCooldowns[i]) / maxCooldowns[i] * COOLDOWN_WIDTH);
//
//                context.drawText(textRenderer, abilityName, globalX, globalY, 0xffffff, true);
//                context.drawTexture(COOLDOWN_EMPTY, globalX + textLength, globalY, 0, 0, COOLDOWN_WIDTH, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);
//                context.drawTexture(COOLDOWN_FULL, globalX + textLength, globalY, 0, 0, scaledCooldownWidth, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);
//            }
//        }
    }
}


