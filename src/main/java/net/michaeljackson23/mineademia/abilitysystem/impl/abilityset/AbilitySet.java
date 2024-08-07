 package net.michaeljackson23.mineademia.abilitysystem.impl.abilityset;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AbilitySet implements IAbilitySet {

    private final HashSet<IAbility> innerSet;
    private final String name;

    public AbilitySet(@NotNull String name) {
        this.name = name;
        this.innerSet = new HashSet<>();
    }

    public AbilitySet(@NotNull String name, @NotNull IAbility... abilities) {
        this.name = name;
        this.innerSet = new HashSet<>(List.of(abilities));
    }

    public AbilitySet(@NotNull String name, @NotNull IAbilitySet set) {
        this.name = name;
        this.innerSet = new HashSet<>(set);
    }

    public AbilitySet(@NotNull Collection<IAbilitySet> sets) {
        this.name = chainNames(sets);
        this.innerSet = new HashSet<>();

        for (IAbilitySet set : sets)
            this.innerSet.addAll(set);
    }
    public AbilitySet(@NotNull IAbilitySet... sets) {
        this(List.of(sets));
    }

    @Override
    public int size() {
        return innerSet.size();
    }

    @Override
    public boolean isEmpty() {
        return innerSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof IAbility))
            return false;

        return innerSet.stream().anyMatch((a) -> a.getClass().equals(o.getClass()));
    }

    @NotNull
    @Override
    public Iterator<IAbility> iterator() {
        return innerSet.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return innerSet.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return innerSet.toArray(a);
    }

    @Override
    public boolean add(@NotNull IAbility ability) {
        return innerSet.add(ability);
    }

    @Override
    public boolean remove(@NotNull Object o) {
        return innerSet.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return innerSet.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends IAbility> c) {
        return innerSet.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return innerSet.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return innerSet.removeAll(c);
    }

    @Override
    public void clear() {
        innerSet.clear();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        return innerSet.equals(o);
    }

    @Override
    public int hashCode() {
        return innerSet.hashCode();
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public <T extends IActiveAbility> @Nullable T getByType(@NotNull Class<T> type) {
        Optional<IAbility> result = innerSet.stream().filter((a) -> a.getClass().equals(type)).findFirst();
        return result.map(type::cast).orElse(null);
    }

    private static String chainNames(Collection<IAbilitySet> sets) {
        StringBuilder builder = new StringBuilder();

        for (IAbilitySet set : sets) {
            String name = set.getName();

            if (!name.isBlank())
                builder.append(name).append(" & ");
        }
        if (!builder.isEmpty())
            builder.delete(builder.length() - 3, builder.length());

        return builder.toString();
    }

}
