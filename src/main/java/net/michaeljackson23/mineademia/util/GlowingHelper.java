package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class GlowingHelper {

    private GlowingHelper() { }

    public static final int WHITE = 0xffffff;

    private static final HashMap<Entity, Integer> entityGlowColors = new HashMap<>();


    public static void setColor(@NotNull Entity entity, float red, float green, float blue) {
        entityGlowColors.put(entity, MathHelper.packRgb(red, green, blue));
    }

    public static void setColor(@NotNull Entity entity, int color) {
        entityGlowColors.put(entity, color);
    }

    public static int getColor(@NotNull Entity entity) {
        return entityGlowColors.getOrDefault(entity, WHITE);
    }

    public static boolean hasColor(@NotNull Entity entity) {
        return entityGlowColors.containsKey(entity);
    }

    public static void clearColor(@NotNull Entity entity) {
        entityGlowColors.remove(entity);
    }

}
