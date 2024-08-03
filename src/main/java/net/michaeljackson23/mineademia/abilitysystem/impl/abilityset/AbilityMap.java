package net.michaeljackson23.mineademia.abilitysystem.impl.abilityset;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AbilityMap extends HashMap<Class<? extends IAbility>, IAbility> implements IAbilityMap {

    public AbilityMap() {

    }
    public AbilityMap(@NotNull IAbilitySet set) {
        setAbilities(set);
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

}
