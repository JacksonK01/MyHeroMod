package net.michaeljackson23.mineademia.client.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityHudManager;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityUserPacketS2C;
import net.michaeljackson23.mineademia.abilitysystem.networking.NetworkKeys;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

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

    private static final int SPACING_Y = 3;
    private static final int SPACING_X = 10;

    public static final int INCREMENT_Y = COOLDOWN_HEIGHT + SPACING_Y;


    public static void display(DrawContext context, float tick) {
        AbilityUserPacketS2C self = ClientCache.self;

        int drawHeight = SPACING_Y;

        drawHeight = drawStamina(context, tick, drawHeight);
        drawHeight = drawCooldowns(context, tick, drawHeight);

        // Draws the custom HUD given the user's abilities
        AbilityHudManager.drawUserHud(context, tick);
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
//        int globalY = height - STAMINA_HEIGHT - SPACING_Y;
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
//        int yIncrement = COOLDOWN_HEIGHT + SPACING_Y;
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

    private static int drawStamina(@NotNull DrawContext context, float tick, int drawHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        AbilityUserPacketS2C self = ClientCache.self;
        if (self == null)
            return drawHeight;

//        int height = client.getWindow().getScaledHeight();
        int drawWidth = SPACING_X;

        int staminaWidth = (int) (( (float) self.getStamina() / self.getMaxStamina()) * STAMINA_WIDTH);

        Text staminaText = Text.literal("Stamina: ");
        int staminaTextLength = textRenderer.getWidth(staminaText);

        context.drawText(textRenderer, staminaText, drawWidth, drawHeight, 0xffffff, true);
        context.drawTexture(STAMINA_EMPTY, drawWidth + staminaTextLength, drawHeight, 0, 0, STAMINA_WIDTH, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);
        context.drawTexture(STAMINA_FULL, drawWidth + staminaTextLength, drawHeight, 0, 0, staminaWidth, STAMINA_HEIGHT, STAMINA_WIDTH, STAMINA_HEIGHT);

        return drawHeight;
    }

    private static int drawCooldowns(@NotNull DrawContext context, float tick, int drawHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        AbilityUserPacketS2C self = ClientCache.self;
        if (self == null)
            return drawHeight;

        HashSet<IReadonlyTypesafeMap> displayedAbilities = new HashSet<>();
        self.getAbilityMap().forEach((type, ability) -> { if (shouldDisplay(type, ability)) displayedAbilities.add(ability); } );

        Optional<IReadonlyTypesafeMap> longestAbility = displayedAbilities.stream().max(Comparator.comparingInt(a -> a.getAndCompute(NetworkKeys.NAME, String::length, 0)));


        Text longestText = Text.literal((longestAbility.map(iReadonlyTypesafeMap -> iReadonlyTypesafeMap.getOrDefault(NetworkKeys.NAME, "")).orElse("")));
        int longestTextLength = textRenderer.getWidth(longestText);

        for (IReadonlyTypesafeMap ability : displayedAbilities)
            drawHeight = drawCooldown(context, tick, drawHeight, longestTextLength, ability);

        return drawHeight;
    }

    private static boolean shouldDisplay(@NotNull Class<?> abilityTYpe, @NotNull IReadonlyTypesafeMap ability) {
        int cooldownTicksRemaining = ability.getOrDefault(NetworkKeys.COOLDOWN_TICKS_REMAINING, 0); // if no cooldown present, never display
        return cooldownTicksRemaining != 0;
    }

    private static int drawCooldown(@NotNull DrawContext context, float tick, int drawHeight, int longestTextLength, @NotNull IReadonlyTypesafeMap ability) {
        // if not ability cooldown, return
        if (!ability.getAndCompute(NetworkKeys.TYPE, ICooldownAbility.class::isAssignableFrom, false))
            return drawHeight;

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int drawWidth = SPACING_X;
        drawHeight += INCREMENT_Y;

        String abilityName = ability.getOrDefault(NetworkKeys.NAME, "");
//        Text nameText = Text.literal(abilityName + ": ");

        int cooldownTicks = ability.getOrDefault(NetworkKeys.COOLDOWN_TICKS, 0);
        int cooldownTicksRemaining = ability.getOrDefault(NetworkKeys.COOLDOWN_TICKS_REMAINING, 0);

        int partial = (int) ((float) Math.min(cooldownTicksRemaining, cooldownTicks) / cooldownTicks * COOLDOWN_WIDTH);

        context.drawText(textRenderer, abilityName, drawWidth, drawHeight, 0xffffff, true);
        context.drawTexture(COOLDOWN_EMPTY, drawWidth + longestTextLength, drawHeight, 0, 0, COOLDOWN_WIDTH, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);
        context.drawTexture(COOLDOWN_FULL, drawWidth + longestTextLength, drawHeight, 0, 0, partial, COOLDOWN_HEIGHT, COOLDOWN_WIDTH, COOLDOWN_HEIGHT);

        return drawHeight;
    }

}


