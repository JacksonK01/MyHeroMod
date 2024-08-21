package net.michaeljackson23.mineademia.abilitysystem.intr.abilityset;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;

public interface IAbilityMap extends Map<Class<? extends IAbility>, IAbility> {

    <T extends IAbility> T get(@NotNull Class<T> key);

    void setAbilities(@NotNull IAbilitySet set);

    @NotNull
    <T extends IAbility> HashSet<T> getAbilities(@NotNull Class<T> type, boolean exact);

}
