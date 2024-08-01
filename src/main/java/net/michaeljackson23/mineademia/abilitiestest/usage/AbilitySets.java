package net.michaeljackson23.mineademia.abilitiestest.usage;

import net.michaeljackson23.mineademia.abilitiestest.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.DodgeAbility;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.quirks.hchh.cold.IceShootAbility;
import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.quirks.hchh.cold.IceSnowflakeAbility;

import java.util.function.Function;

public final class AbilitySets {

    private AbilitySets() { }


    public static final Function<IAbilityUser, IAbilitySet> GENERAL = (u) -> new AbilitySet(new DodgeAbility(u));


    public static final Function<IAbilityUser, IAbilitySet> OFA = (u) -> new AbilitySet();

    public static final Function<IAbilityUser, IAbilitySet> EXPLOSION = (u) -> new AbilitySet();

    public static final Function<IAbilityUser, IAbilitySet> RIFLE = (u) -> new AbilitySet();

    public static final Function<IAbilityUser, IAbilitySet> HCHH_HOT = (u) -> new AbilitySet();
    public static final Function<IAbilityUser, IAbilitySet> HCHH_COLD = (u) -> new AbilitySet(new IceShootAbility(u), new IceSnowflakeAbility(u));

}
