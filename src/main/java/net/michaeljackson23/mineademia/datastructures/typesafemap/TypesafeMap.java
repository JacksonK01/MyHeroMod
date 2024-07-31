package net.michaeljackson23.mineademia.datastructures.typesafemap;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

public final class TypesafeMap implements ITypesafeMap {

    private final IdentityHashMap<Key<?>, Object> backend;

    public TypesafeMap() {
        this.backend = new IdentityHashMap<>();
    }

    @Override
    public boolean isEmpty() {
        return backend.isEmpty();
    }

    @Override
    public <T> boolean containsKey(@NotNull Key<T> key) {
        return backend.containsKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@NotNull Key<T> key) {
        Object raw = backend.get(key);
        return (T) raw;
    }

    public <T> Key<T> createEntry(T value) {
        Key<T> key = new Key<>();
        putIfAbsent(key, value);

        return key;
    }

    @Override
    public <T> void put(@NotNull Key<T> key, T value) {
        backend.put(key, value);
    }

    @Override
    public <T> boolean remove(@NotNull Key<T> key) {
        if (backend.containsKey(key)) {
            backend.remove(key);
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        backend.clear();
    }

    public Set<Key<?>> getKeys() {
        return backend.keySet();
    }
    public Collection<Object> getValues() {
        return backend.values();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Key<?> key : getKeys()) {
            Object raw = backend.get(key);
            builder.append(key.hashCode()).append(" - ").append(Objects.toString(raw, "null")).append('\n');
        }

        return builder.toString();
    }

}
