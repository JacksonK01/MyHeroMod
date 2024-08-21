package net.michaeljackson23.mineademia.abilitysystem.impl.abilityset;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;

public class AbilityMap extends HashMap<Class<? extends IAbility>, IAbility> implements IAbilityMap {

    public AbilityMap() {

    }
    public AbilityMap(@NotNull IAbilitySet set) {
        setAbilities(set);
    }
    public AbilityMap(@NotNull IAbilityMap map) {
        super.putAll(map);
    }

    @Nullable
    public <T extends IAbility> T get(@NotNull Class<T> key) {
        IAbility result = super.get(key);
        if (result == null)
            return null;

        return key.cast(result);
    }

    public void setAbilities(@NotNull IAbilitySet set) {
        clear();
        for (IAbility ability : set)
            put(ability.getClass(), ability);
    }

    @Override
    public @NotNull <T extends IAbility> HashSet<T> getAbilities(@NotNull Class<T> type, boolean exact) {
        HashSet<T> result = new HashSet<>();
        forEach((t, a) -> { if (isMatchingRequirements(t, type, exact)) result.add(type.cast(a)); });

        return result;
    }

    public static <T extends IAbility> boolean isMatchingRequirements(@NotNull Class<?> abilityType, @NotNull Class<T> type, boolean exact) {
        if (exact)
            return abilityType.equals(type);
        else
            return type.isAssignableFrom(abilityType);
    }

}
