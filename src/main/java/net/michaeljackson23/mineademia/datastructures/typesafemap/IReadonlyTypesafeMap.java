package net.michaeljackson23.mineademia.datastructures.typesafemap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IReadonlyTypesafeMap {

    boolean isEmpty();

    <T> boolean containsKey(@NotNull Key<T> key);

    @Nullable
    <T> T get(@NotNull Key<T> key);

    default <T> T getOrDefault(@NotNull Key<T> key, @Nullable T defaultValue) {
        T result = get(key);
        return result == null ? defaultValue : result;
    }
    default <T> T getOrCompute(@NotNull Key<T> key, @NotNull Supplier<T> defaultValue) {
        return getOrDefault(key, defaultValue.get());
    }
    default  <T, O> O getAndCompute(@NotNull Key<T> key, @NotNull Function<T, O> validCompute, @Nullable O defaultValue) {
        T value = get(key);
        return value == null ? defaultValue : validCompute.apply(value);
    }

    default <T> boolean getAndDo(@NotNull Key<T> key, @NotNull Consumer<T> action) {
        T value = get(key);
        if (value != null) {
            action.accept(value);
            return true;
        }

        return false;
    }

    final class Key<T> { }

}
