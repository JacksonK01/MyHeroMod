package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.FireRifleAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.LoadAmmoAbility;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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

    private static void loadAmmoAbility(@NotNull DrawContext context, float tick, @NotNull IReadonlyTypesafeMap ability) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int width = client.getWindow().getScaledWidth();

        int yOffset = 20;
        int bulletSpacing = 10;
        int bulletSize = 5;

        float halfBullets = LoadAmmoAbility.MAX_BULLET_AMOUNT / 2f;
        int drawWidth = (int) ((width / 2f) - (halfBullets * bulletSize + (Math.max(0, halfBullets - 0.5f)) * bulletSpacing));

        int[] amounts = new int[LoadAmmoAbility.AmmoType.values().length];
        if (amounts.length == 0)
            return;

        amounts[0] = ability.getOrDefault(NetworkKeys.REGULAR_AMOUNT, 0);
        amounts[1] = ability.getOrDefault(NetworkKeys.HOLLOW_AMOUNT, 0);

        int amountIndex = 0;
        int amountDrawn = 0;

        for (int i = 0; i < LoadAmmoAbility.MAX_BULLET_AMOUNT; i++) {
            while (amountIndex < amounts.length && amountDrawn++ >= amounts[amountIndex]) {
                amountIndex++;
                amountDrawn = 0;
            }

            int outlineColor = switch (amountIndex) {
                case 0 -> 0x0000ff; // Regular
                case 1 -> 0xff0000; // Hollow
                default -> 0xffffff; // Empty
            };

            context.drawText(textRenderer, "I", drawWidth, yOffset, outlineColor, true);
            drawWidth += bulletSize + bulletSpacing;
        }
    }

    private static void fireRifleAbility(DrawContext context, float tick, @NotNull IReadonlyTypesafeMap ability) {

    }

}
