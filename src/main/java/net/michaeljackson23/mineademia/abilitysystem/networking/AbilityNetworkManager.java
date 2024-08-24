package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IActivationAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.datastructures.typesafemap.TypesafeMap;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class AbilityNetworkManager {

    private AbilityNetworkManager() {}

    private static final HashMap<Class<? extends IAbility>, AbilityRegister<?>> registerMap =  new HashMap<>();

    @Environment(EnvType.CLIENT)
    public static final HashMap<String, Class<?>> classMap = new HashMap<>();


    public static void register() {
        registerAbility(IAbility.class)
                .add(new AbilityRegisterEntry<>(IAbility::getName, AbilityKeys.NAME, false, AbilityRegisterMethod.STRING))
                .add(new AbilityRegisterEntry<>(IAbility::getDescription, AbilityKeys.DESCRIPTION, false, AbilityRegisterMethod.STRING))
                .add(new AbilityRegisterEntry<>(IAbility::canExecute, AbilityKeys.CAN_EXECUTE, true, AbilityRegisterMethod.BOOLEAN));

        registerAbility(IActivationAbility.class)
                .add(new AbilityRegisterEntry<>(IActivationAbility::isActive, AbilityKeys.IS_ACTIVE, true, AbilityRegisterMethod.BOOLEAN))
                .add(new AbilityRegisterEntry<>(IActivationAbility::getTicks, AbilityKeys.GET_TICKS, true, AbilityRegisterMethod.INT));

        registerAbility(ICooldownAbility.class)
                .add(new AbilityRegisterEntry<>((a) -> a.getCooldown().getCooldownTicks(), AbilityKeys.COOLDOWN_TICKS, true, AbilityRegisterMethod.INT))
                .add(new AbilityRegisterEntry<>((a) -> a.getCooldown().getTicksRemaining(), AbilityKeys.COOLDOWN_TICKS_REMAINING, true, AbilityRegisterMethod.INT));
    }

    @SuppressWarnings("unchecked")
    public static <AT extends IAbility> AbilityRegister<AT> registerAbility(@NotNull Class<AT> abilityType, boolean override) {
        if (override || !registerMap.containsKey(abilityType))
            registerMap.put(abilityType, new AbilityRegister<>(abilityType));

        return (AbilityRegister<AT>) registerMap.get(abilityType);
    }
    public static <AT extends IAbility> AbilityRegister<AT> registerAbility(@NotNull Class<AT> abilityType) {
        return registerAbility(abilityType, false);
    }

    public static <T extends IAbility> void encode(@NotNull T ability, @NotNull PacketByteBuf buffer, boolean minimal) {
        Class<?> type = ability.getClass();
        // classMap.putIfAbsent(type.getName(), ability.getClass());

        buffer.writeString(type.getName());
        encode(ability, type, buffer, minimal, new HashSet<>());
    }
    private static void encode(IAbility ability, @NotNull Class<?> type, @NotNull PacketByteBuf buffer, boolean minimal, @NotNull HashSet<Class<?>> encodedTypes) {
        if (!encodedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            encode(ability, superType, buffer, minimal, encodedTypes);

        for (Class<?> interfaceType : type.getInterfaces())
            encode(ability, interfaceType, buffer, minimal, encodedTypes);

        if (registerMap.containsKey(type))
            registerMap.get(type).tryEncode(ability, buffer, minimal);
    }

    public static <T extends IAbility> @NotNull IReadonlyTypesafeMap decode(@NotNull PacketByteBuf buffer, boolean minimal) {
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

        map.put(AbilityKeys.TYPE, type);

        decode(type, buffer, map, minimal, new HashSet<>());
        return map;
    }
    public static void decode(@NotNull Class<?> type, @NotNull PacketByteBuf buffer, @NotNull TypesafeMap map, boolean minimal, @NotNull HashSet<Class<?>> decodedTypes) {
        if (!decodedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            decode(superType, buffer, map, minimal, decodedTypes);

        for (Class<?> interfaceType : type.getInterfaces())
            decode(interfaceType, buffer, map, minimal, decodedTypes);

        if (registerMap.containsKey(type))
            registerMap.get(type).decode(buffer, map, minimal);
    }


    public record AbilityRegisterEntry<AT extends IAbility, ET>(Function<AT, ET> serverGetter, IReadonlyTypesafeMap.Key<ET> clientKey, boolean minimal, AbilityRegisterMethod<ET> registerMethod) {

        public void encode(@NotNull AT ability, @NotNull PacketByteBuf buffer) {
            registerMethod().bufferEncoder().accept(buffer, serverGetter().apply(ability));
        }

        public void decode(@NotNull PacketByteBuf buffer, @NotNull TypesafeMap map) {
            map.put(clientKey(), registerMethod().bufferDecoder().apply(buffer));
        }

    }
    public record AbilityRegisterMethod<ET>(Class<ET> entryType, BiConsumer<PacketByteBuf, ET> bufferEncoder, Function<PacketByteBuf, ET> bufferDecoder) {

        public static final AbilityRegisterMethod<Boolean> BOOLEAN = new AbilityRegisterMethod<>(Boolean.class, PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

        public static final AbilityRegisterMethod<Integer> INT = new AbilityRegisterMethod<>(Integer.class, PacketByteBuf::writeInt, PacketByteBuf::readInt);
        public static final AbilityRegisterMethod<Long> LONG = new AbilityRegisterMethod<>(Long.class, PacketByteBuf::writeLong, PacketByteBuf::readLong);

        public static final AbilityRegisterMethod<Float> FLOAT = new AbilityRegisterMethod<>(Float.class, PacketByteBuf::writeFloat, PacketByteBuf::readFloat);
        public static final AbilityRegisterMethod<Double> DOUBLE = new AbilityRegisterMethod<>(Double.class, PacketByteBuf::writeDouble, PacketByteBuf::readDouble);

        public static final AbilityRegisterMethod<String> STRING = new AbilityRegisterMethod<>(String.class, PacketByteBuf::writeString, PacketByteBuf::readString);

        public static final AbilityRegisterMethod<UUID> UUID = new AbilityRegisterMethod<>(UUID.class, PacketByteBuf::writeUuid, PacketByteBuf::readUuid);

    }


    public static final class AbilityRegister<AT extends IAbility> {

        private final Class<AT> abilityType;
        private final LinkedHashSet<AbilityRegisterEntry<AT, ?>> entries;

        private AbilityRegister(@NotNull Class<AT> abilityType) {
            this.abilityType = abilityType;
            this.entries = new LinkedHashSet<>();
        }

        @NotNull
        public Class<AT> getAbilityType() {
            return abilityType;
        }

        @NotNull
        public LinkedHashSet<AbilityRegisterEntry<AT, ?>> getEntries() {
            return entries;
        }

        @NotNull
        public <ET> AbilityRegister<AT> add(@NotNull AbilityRegisterEntry<AT, ET> entry) {
            entries.add(entry);
            return this;
        }

        public void encode(@NotNull AT ability, @NotNull PacketByteBuf buffer, boolean minimal) {
            for (AbilityRegisterEntry<AT, ?> entry : entries) {
                if (entry.minimal() || !minimal)
                    entry.encode(ability, buffer);
            }
        }

        public void decode(@NotNull PacketByteBuf buffer, @NotNull TypesafeMap map, boolean minimal) {
            for (AbilityRegisterEntry<AT, ?> entry : entries) {
                if (entry.minimal() || !minimal)
                    entry.decode(buffer, map);
            }
        }

        public boolean tryEncode(@NotNull IAbility ability, @NotNull PacketByteBuf buffer, boolean minimal) {
            if (abilityType.isAssignableFrom(ability.getClass())) {
                AT castAbility = abilityType.cast(ability);
                encode(castAbility, buffer, minimal);
                return true;
            }

            return false;
        }

    }

}
