package net.michaeljackson23.mineademia.abilitysystem.usage;

import net.michaeljackson23.mineademia.abilitysystem.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.DodgeAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.NoClipAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example.ExampleAbility1;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example.ExampleAbility2;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example.ExampleAbility3;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.theworld.TimeStopAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.engine.EngineDashPassive;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion.ApShotAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion.ExplodeAPult;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion.ExplosiveSpeedAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion.HowitzerImpactAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold.IceBeamAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold.IceShootAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold.IceSnowflakeAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold.IceSpikeAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.miscellaneous.SuperHeroLandingAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.*;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.AirwalkAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.FireRifleAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.LoadAmmoAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.SuperchargedShotAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive.EnhancedEyesightAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive.HairAmmoAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind.*;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold.IceWallAbility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public final class AbilitySets {

    private AbilitySets() { }

    private static final HashMap<String, Function<IAbilityUser, IAbilitySet>> abilitySetMap = new HashMap<>();

    public static final Function<IAbilityUser, IAbilitySet> GENERAL = registerAbilitySet("general", (u) -> new AbilitySet("", new DodgeAbility(u), new NoClipAbility(u)));


    public static final Function<IAbilityUser, IAbilitySet> OFA = registerAbilitySet("ofa", (u) -> new AbilitySet("One For All", new AirForceAbility(u), new FullCowlingAbility(u), new ManchesterSmashAbility(u), new PickVestigeAbility(u), new DetroitSmashAbility(u)));

    public static final Function<IAbilityUser, IAbilitySet> EXPLOSION = registerAbilitySet("explosion", (u) -> new AbilitySet("Explosion", new ExplosiveSpeedAbility(u), new ApShotAbility(u), new HowitzerImpactAbility(u), new ExplodeAPult(u)));

    public static final Function<IAbilityUser, IAbilitySet> RIFLE = registerAbilitySet("rifle", (u) -> new AbilitySet("Rifle", new EnhancedEyesightAbility(u), new HairAmmoAbility(u), new AirwalkAbility(u), new FireRifleAbility(u), new LoadAmmoAbility(u), new SuperchargedShotAbility(u)));

    public static final Function<IAbilityUser, IAbilitySet> HCHH_HOT = registerAbilitySet("hchh_hot", (u) -> new AbilitySet("Half Cold Half Hot"));
    public static final Function<IAbilityUser, IAbilitySet> HCHH_COLD = registerAbilitySet("hchh_cold", (u) -> new AbilitySet("Half Cold Half Hot", new IceShootAbility(u), new IceSnowflakeAbility(u), new IceBeamAbility(u),  new IceSpikeAbility(u), new IceWallAbility(u)));

    public static final Function<IAbilityUser, IAbilitySet> WHIRLWIND = registerAbilitySet("whirlwind", (u) -> new AbilitySet("Whirlwind", new WindBladeAbility(u), new TornadoAbility(u), new GaleUpliftAbility(u), new BallistaAbility(u) ,new WindFlyAbility(u)));

    public static final Function<IAbilityUser, IAbilitySet> ENGINE = registerAbilitySet("engine", (u) -> new AbilitySet("Engine", new EngineDashPassive(u), new SuperHeroLandingAbility(u)));

    public static final Function<IAbilityUser, IAbilitySet> EXAMPLE = registerAbilitySet("example", (u) -> new AbilitySet("Example", new ExampleAbility1(u), new ExampleAbility2(u), new ExampleAbility3(u), new TimeStopAbility(u)));

    private static Function<IAbilityUser, IAbilitySet> registerAbilitySet(String key, Function<IAbilityUser, IAbilitySet> abilitySetFunction) {
        abilitySetMap.put(key, abilitySetFunction);
        return abilitySetFunction;
    }

    public static HashMap<String, Function<IAbilityUser, IAbilitySet>> getAbilitySetMap() {
        return abilitySetMap;
    }

    @Nullable
    public static Function<IAbilityUser, IAbilitySet> getAbilitySet(@NotNull String name) {
        return abilitySetMap.get(name);
    }

    @NotNull
    public static Function<IAbilityUser, IAbilitySet> getAbilitySetOrEmpty(@NotNull String name) {
        return abilitySetMap.getOrDefault(name, (u) -> new AbilitySet("Unknown"));
    }

}
