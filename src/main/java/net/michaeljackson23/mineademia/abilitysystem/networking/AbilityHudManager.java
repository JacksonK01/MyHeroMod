package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.FireRifleAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.LoadAmmoAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.mixin.accessors.DrawContextAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public final class AbilityHudManager {

    private AbilityHudManager() { }


    private static final HashMap<Class<?>, TriConsumer<DrawContext, Float, IReadonlyTypesafeMap>> hudMap = new HashMap<>();


    public static void register() {
        // RIFLE
        hudMap.put(LoadAmmoAbility.class, AbilityHudManager::loadAmmoAbility);
        hudMap.put(FireRifleAbility.class, AbilityHudManager::fireRifleAbility);
    }

    public static void drawUserHud(@NotNull DrawContext context, float tick) {
        if (ClientCache.self == null)
            return;

        HashMap<Class<?>, IReadonlyTypesafeMap> abilities = ClientCache.self.getAbilityMap();

        for (Class<?> abilityType : abilities.keySet())
            drawAbilityHud(context, tick, abilities.get(abilityType), abilityType, new HashSet<>());
    }
    private static void drawAbilityHud(@NotNull DrawContext context, float tick, @NotNull IReadonlyTypesafeMap ability, @NotNull Class<?> type, @NotNull HashSet<Class<?>> iteratedTypes) {
        if (!iteratedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            drawAbilityHud(context, tick, ability, superType, iteratedTypes);

        for (Class<?> interfaceType : type.getInterfaces())
            drawAbilityHud(context, tick, ability, interfaceType, iteratedTypes);

        if (hudMap.containsKey(type))
            hudMap.get(type).accept(context, tick, ability);
    }


    // ---------- RIFLE ----------

    public static final Identifier RIFLE_TEXTURE_BULLET = new Identifier(Mineademia.MOD_ID, "textures/hud/quirk_rifle_bullet.png");

    public static final float RIFLE_TEXTURE_ALPHA_FULL = 0.25f;
    public static final float RIFLE_TEXTURE_ALPHA_EMPTY = 0.45f;

    public static final int RIFLE_Y_OFFSET = 10;
    public static final int RIFLE_BULLET_SPACING = -5;
    public static final int RIFLE_BULLET_SIZE = 20;
    public static final int RIFLE_INCREASE = RIFLE_BULLET_SPACING + RIFLE_BULLET_SIZE;

    public static final int RIFLE_BULLET_TEXT_SCALE = 2;
    public static final float RIFLE_HALF_BULLETS = (LoadAmmoAbility.AmmoType.values().length + 2) / 2f;

    public static final int RIFLE_DRAW_WIDTH = (int) (RIFLE_HALF_BULLETS * RIFLE_BULLET_SIZE + (Math.max(0, RIFLE_HALF_BULLETS - 0.5f) * RIFLE_BULLET_SPACING));

    private static void loadAmmoAbility(@NotNull DrawContext context, float tick, @NotNull IReadonlyTypesafeMap ability) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int width = client.getWindow().getScaledWidth();
        int drawWidth = (int) ((width / 2f) - RIFLE_DRAW_WIDTH);

        LoadAmmoAbility.AmmoType[] values = LoadAmmoAbility.AmmoType.values();
        if (values.length == 0)
            return;

        int[] amounts = new int[values.length];

        for (int i = 0; i < values.length; i++)
            amounts[i] = ability.getOrDefault(values[i].getNetworkKey(), 0);

        for (int i = 0; i < amounts.length; i++) {
            int outlineColor = LoadAmmoAbility.AmmoType.getColor(i);

            if (amounts[i] > 0) {
                context.drawTexture(RIFLE_TEXTURE_BULLET, drawWidth, RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE);
                drawTextureColored(context, RIFLE_TEXTURE_BULLET, drawWidth, RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, 1, outlineColor, RIFLE_TEXTURE_ALPHA_FULL);

                MatrixStack matrix = context.getMatrices();
                matrix.push();
                matrix.scale(1f / RIFLE_BULLET_TEXT_SCALE, 1f / RIFLE_BULLET_TEXT_SCALE, 1f);

                context.drawText(textRenderer, "x" + amounts[i], drawWidth * RIFLE_BULLET_TEXT_SCALE, RIFLE_Y_OFFSET * RIFLE_BULLET_TEXT_SCALE, 0xffffff, true);

                matrix.pop();
            } else
                drawTextureColored(context, RIFLE_TEXTURE_BULLET, drawWidth, RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, 1, outlineColor, RIFLE_TEXTURE_ALPHA_EMPTY);

            drawWidth += RIFLE_INCREASE;
        }
    }

    private static void fireRifleAbility(DrawContext context, float tick, @NotNull IReadonlyTypesafeMap ability) {
        MinecraftClient client = MinecraftClient.getInstance();

        int width = client.getWindow().getScaledWidth();
        int drawWidth = (int) ((width / 2f) - RIFLE_DRAW_WIDTH);

        int color = LoadAmmoAbility.AmmoType.DEFAULT_COLOR;

        LoadAmmoAbility.AmmoType[] values = LoadAmmoAbility.AmmoType.values();

        boolean isLoaded = ability.getOrDefault(NetworkKeys.AMMO_LOADED, false);
        if (isLoaded) {
            LoadAmmoAbility.AmmoType ammoType = values[ability.getOrDefault(NetworkKeys.AMMO_TYPE, 0)];
            color = ammoType.getColor();

            context.drawTexture(RIFLE_TEXTURE_BULLET, drawWidth + (int) (RIFLE_INCREASE * (values.length + 0.5f)), RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE);
            drawTextureColored(context, RIFLE_TEXTURE_BULLET, drawWidth + (int) (RIFLE_INCREASE * (values.length + 0.5f)), RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, 1, color, RIFLE_TEXTURE_ALPHA_FULL);
        } else
            drawTextureColored(context, RIFLE_TEXTURE_BULLET, drawWidth + (int) (RIFLE_INCREASE * (values.length + 0.5f)), RIFLE_Y_OFFSET, 0, 0, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, RIFLE_BULLET_SIZE, 1, color, RIFLE_TEXTURE_ALPHA_EMPTY);
    }

    // Helper

    public static void drawTextureColored(@NotNull DrawContext context, @NotNull Identifier identifier, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int z, int color, float alpha) {
        ((DrawContextAccessor) context).invokeDrawTexturedQuad(identifier, x, x + width, y, y + height, z, u / (float) textureWidth, (u + width) / (float) textureWidth, v / (float) textureHeight, (v + height) / (float) textureHeight, ColorHelper.Argb.getRed(color) / 255f, ColorHelper.Argb.getGreen(color) / 255f, ColorHelper.Argb.getBlue(color) / 255f, alpha);
    }

}
