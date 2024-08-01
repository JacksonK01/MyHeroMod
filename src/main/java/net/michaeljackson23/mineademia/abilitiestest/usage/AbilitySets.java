package net.michaeljackson23.mineademia.abilitiestest.usage;

import net.michaeljackson23.mineademia.abilitiestest.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.DodgeAbility;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.quirks.hchh.cold.IceShootAbility;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.quirks.hchh.cold.IceSnowflakeAbility;

import java.util.HashMap;
import java.util.function.Function;

public final class AbilitySets {

    private AbilitySets() { }

    private static final HashMap<String, Function<IAbilityUser, IAbilitySet>> abilitySetMap = new HashMap<>();

    public static final Function<IAbilityUser, IAbilitySet> GENERAL = registerAbilitySet("general", (u) -> new AbilitySet(new DodgeAbility(u)));


    public static final Function<IAbilityUser, IAbilitySet> OFA = registerAbilitySet("ofa", (u) -> new AbilitySet());

    public static final Function<IAbilityUser, IAbilitySet> EXPLOSION = registerAbilitySet("explosion", (u) -> new AbilitySet());

    public static final Function<IAbilityUser, IAbilitySet> RIFLE = registerAbilitySet("rifle", (u) -> new AbilitySet());

    public static final Function<IAbilityUser, IAbilitySet> HCHH_HOT = registerAbilitySet("hchh_hot", (u) -> new AbilitySet());
    public static final Function<IAbilityUser, IAbilitySet> HCHH_COLD = registerAbilitySet("hchh_cold", (u) -> new AbilitySet(new IceShootAbility(u), new IceSnowflakeAbility(u)));

    private static Function<IAbilityUser, IAbilitySet> registerAbilitySet(String key, Function<IAbilityUser, IAbilitySet> abilitySetFunction) {
        abilitySetMap.put(key, abilitySetFunction);
        return abilitySetFunction;
    }

    public static HashMap<String, Function<IAbilityUser, IAbilitySet>> getAbilitySetMap() {
        return abilitySetMap;
    }
}
