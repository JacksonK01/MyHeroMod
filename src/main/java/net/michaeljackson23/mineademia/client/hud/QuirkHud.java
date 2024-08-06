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
    public static boolean showAllCooldowns = false;

    private static final Identifier STAMINA_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_full.png");
    private static final Identifier STAMINA_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/stamina_empty.png");

    private static final Identifier COOLDOWN_EMPTY = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_empty.png");
    private static final Identifier COOLDOWN_FULL = new Identifier(Mineademia.MOD_ID, "textures/hud/cooldown_full.png");

    private static final int STAMINA_WIDTH = 80;
    private static final int STAMINA_HEIGHT = 8;

    private static final int COOLDOWN_WIDTH = 80;
    private static final int COOLDOWN_HEIGHT = 8;

    private static final int SPACING = 5;

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
        int staminaWidth = (int) ((double) abilityData.getStamina() / abilityData.getMaxStamina() * STAMINA_WIDTH);

        context.drawTexture(STAMINA_EMPTY, globalX, globalY, 0, 0, STAMINA_WIDTH, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
        context.drawTexture(STAMINA_FULL, globalX, globalY, 0, 0, staminaWidth, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);

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


