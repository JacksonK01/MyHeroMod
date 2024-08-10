package net.michaeljackson23.mineademia.util;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public final class EntityReflection {

    private EntityReflection() { }

    static {
        try {
            setFlagMethod = registerMethod(Entity.class, "setFlag", int.class, boolean.class);
            getFlagMethod = registerMethod(Entity.class, "getFlag", int.class);

            setLivingFlagMethod = registerMethod(LivingEntity.class, "setLivingFlag", int.class, boolean.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private static final Method setFlagMethod;
    private static final Method getFlagMethod;

    private static final Method setLivingFlagMethod;


    public static void register() {
        Mineademia.LOGGER.info("Registered EntityReflection for " + Mineademia.MOD_ID);
    }

    @NotNull
    @Contract(pure = true)
    private static Method registerMethod(@NotNull @NonNls Class<?> type, @NotNull String name, @Nullable Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = type.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);

        return method;
    }

    public static boolean trySetFlag(@NotNull Entity entity, int mask, boolean value) {
        try {
            setFlagMethod.invoke(entity, mask, value);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }
    public static boolean tryGetFlag(@NotNull Entity entity, int mask, AtomicBoolean returnResult) {
        try {
            Object result = getFlagMethod.invoke(entity, mask);
            returnResult.set((boolean) result);

            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    public static boolean trySetLivingFlag(@NotNull LivingEntity entity, int mask, boolean value) {
        try {
            setLivingFlagMethod.invoke(entity, mask, value);
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

}
