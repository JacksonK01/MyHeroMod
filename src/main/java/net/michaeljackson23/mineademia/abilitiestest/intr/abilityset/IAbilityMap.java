package net.michaeljackson23.mineademia.abilitiestest.intr.abilityset;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IAbility;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IAbilityMap extends Map<Class<? extends IAbility>, IAbility> {

    <T extends IAbility> T get(@NotNull Class<T> key);

    void setAbilities(@NotNull IAbilitySet set);

}
