package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;

import java.util.UUID;

public final class NetworkKeys {

    private NetworkKeys() { }


    // Global
    public static final IReadonlyTypesafeMap.Key<Class<?>> TYPE = new IReadonlyTypesafeMap.Key<>();

    // IAbilityUser
    public static final IReadonlyTypesafeMap.Key<Integer> ABILITY_AMOUNT = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> MAX_STAMINA = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> STAMINA = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Boolean> ENABLED = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Boolean> BLOCKED = new IReadonlyTypesafeMap.Key<>();

    // IAbility, IAbilityUser
    public static final IReadonlyTypesafeMap.Key<UUID> UUID = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> ID = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<String> NAME = new IReadonlyTypesafeMap.Key<>();

    // IAbility
    public static final IReadonlyTypesafeMap.Key<String> DESCRIPTION = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Boolean> CAN_EXECUTE = new IReadonlyTypesafeMap.Key<>();

    // IActiveAbility
    public static final IReadonlyTypesafeMap.Key<AbilityCategory[]> ABILITY_CATEGORIES = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<AbilityCategory[]> BLOCKING_CATEGORIES = new IReadonlyTypesafeMap.Key<>();

    // IActivationAbility, IPassiveToggleAbility
    public static final IReadonlyTypesafeMap.Key<Boolean> IS_ACTIVE = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> GET_TICKS = new IReadonlyTypesafeMap.Key<>();

    // ICooldownAbility
    public static final IReadonlyTypesafeMap.Key<Integer> COOLDOWN_TICKS = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> COOLDOWN_TICKS_REMAINING = new IReadonlyTypesafeMap.Key<>();

}
