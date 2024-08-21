package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.impl.server.ability.passive.TogglePassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IActivationAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.datastructures.typesafemap.TypesafeMap;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class AbilityDecoders {

    private AbilityDecoders() { }

    public static final IReadonlyTypesafeMap.Key<Class<?>> ABILITY_TYPE = new IReadonlyTypesafeMap.Key<>();

    public static final IReadonlyTypesafeMap.Key<String> ABILITY_NAME = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<String> ABILITY_DESCRIPTION = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Boolean> ABILITY_CAN_EXECUTE = new IReadonlyTypesafeMap.Key<>();

    public static final IReadonlyTypesafeMap.Key<AbilityCategory[]> ACTIVE_ABILITY_CATEGORIES = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<AbilityCategory[]> ACTIVE_ABILITY_BLOCKING_CATEGORIES = new IReadonlyTypesafeMap.Key<>();

    public static final IReadonlyTypesafeMap.Key<Boolean> ACTIVATION_ABILITY_IS_ACTIVE = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> ACTIVATION_ABILITY_GET_TICKS = new IReadonlyTypesafeMap.Key<>();

    public static final IReadonlyTypesafeMap.Key<Integer> COOLDOWN_ABILITY_COOLDOWN_TICKS = new IReadonlyTypesafeMap.Key<>();
    public static final IReadonlyTypesafeMap.Key<Integer> COOLDOWN_ABILITY_TICKS_REMAINING = new IReadonlyTypesafeMap.Key<>();

    public static final IReadonlyTypesafeMap.Key<Boolean> PASSIVE_TOGGLE_ABILITY_IS_ACTIVE = new IReadonlyTypesafeMap.Key<>();


    public static final HashMap<String, Class<?>> classMap = new HashMap<>();
    public static final HashMap<Class<? extends IAbility>, BiConsumer<TypesafeMap, PacketByteBuf>> decoderMap = new HashMap<>();


    public static void registerDecoders() {
        registerDecoder(IAbility.class, AbilityDecoders::decodeAbility);
        registerDecoder(IActiveAbility.class, AbilityDecoders::decodeActiveAbility);
        registerDecoder(IActivationAbility.class, AbilityDecoders::decodeActivationAbility);

        registerDecoder(ICooldownAbility.class, AbilityDecoders::decodeCooldownAbility);

        registerDecoder(TogglePassiveAbility.class, AbilityDecoders::decodePassiveToggleAbility);
    }
    private static void registerDecoder(@NotNull Class<? extends IAbility> type, @NotNull BiConsumer<TypesafeMap, PacketByteBuf> decoder) {
        classMap.putIfAbsent(type.getName(), type);
        decoderMap.put(type, decoder);
    }


    public static <T extends IAbility> @NotNull IReadonlyTypesafeMap decode(@NotNull PacketByteBuf buffer) {
        TypesafeMap map = new TypesafeMap();

        String typeName = buffer.readString();
        Class<?> type = classMap.get(typeName);

        if (type == null) {
            try {
                type = Class.forName(typeName);
                classMap.putIfAbsent(type.getName(), type);
            } catch (ClassNotFoundException e) {
                return map;
            }
        }

        map.put(ABILITY_TYPE, type);

        decode(type, buffer, new HashSet<>(), map);
        return map;
    }
    public static void decode(@NotNull Class<?> type, @NotNull PacketByteBuf buffer, @NotNull HashSet<Class<?>> decodedTypes, @NotNull TypesafeMap map) {
        if (!decodedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            decode(superType, buffer, decodedTypes, map);

        for (Class<?> interfaceType : type.getInterfaces())
            decode(interfaceType, buffer, decodedTypes, map);

        if (decoderMap.containsKey(type)) {
            decoderMap.get(type).accept(map, buffer);
        }
    }


    private static void decodeAbility(@NotNull TypesafeMap map, @NotNull PacketByteBuf buffer) {
        map.put(ABILITY_NAME, buffer.readString());
        map.put(ABILITY_DESCRIPTION, buffer.readString());
        map.put(ABILITY_CAN_EXECUTE, buffer.readBoolean());
    }

    private static void decodeActiveAbility(@NotNull TypesafeMap map, @NotNull PacketByteBuf buffer) {
        AbilityCategory[] values = AbilityCategory.values();

//        map.put(ACTIVE_ABILITY_CATEGORIES, Arrays.stream(buffer.readIntArray()).mapToObj((i) -> values[i]).toArray(AbilityCategory[]::new));
//        map.put(ACTIVE_ABILITY_BLOCKING_CATEGORIES, Arrays.stream(buffer.readIntArray()).mapToObj((i) -> values[i]).toArray(AbilityCategory[]::new));
    }

    private static void decodeActivationAbility(@NotNull TypesafeMap map, @NotNull PacketByteBuf buffer) {
        map.put(ACTIVATION_ABILITY_IS_ACTIVE, buffer.readBoolean());
        map.put(ACTIVATION_ABILITY_GET_TICKS, buffer.readInt());
    }

    private static void decodeCooldownAbility(@NotNull TypesafeMap map, @NotNull PacketByteBuf buffer) {
        map.put(COOLDOWN_ABILITY_COOLDOWN_TICKS, buffer.readInt());
        map.put(COOLDOWN_ABILITY_TICKS_REMAINING, buffer.readInt());
    }

    private static void decodePassiveToggleAbility(@NotNull TypesafeMap map, @NotNull PacketByteBuf buffer) {
        map.put(PASSIVE_TOGGLE_ABILITY_IS_ACTIVE, buffer.readBoolean());
    }

}
