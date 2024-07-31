package net.michaeljackson23.mineademia.datastructures.typesafemap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface ITypesafeMap extends IReadonlyTypesafeMap {

    <T> void put(@NotNull Key<T> key, @Nullable T value);

    default <T> boolean putIfAbsent(@NotNull Key<T> key, @Nullable T value) {
        if (!containsKey(key)) {
            put(key, value);
            return true;
        }

        return false;
    }
    default <T> boolean putIfPresent(@NotNull Key<T> key, @Nullable T value) {
        if (containsKey(key)) {
            put(key, value);
            return true;
        }

        return false;
    }
    default <T> boolean putIfDifferent(@NotNull Key<T> key, @Nullable T value) {
        if (containsKey(key)) {
            T previous = get(key);
            if (Objects.equals(value, previous)) {
                put(key, value);
                return true;
            }

            return false;
        }

        put(key, value);
        return true;
    }

    <T> boolean remove(@NotNull Key<T> key);

    void clear();

}
