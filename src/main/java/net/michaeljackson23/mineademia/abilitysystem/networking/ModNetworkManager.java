package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.TogglePassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IActivationAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive.EntityRenderAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.LoadAmmoAbility;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.datastructures.typesafemap.TypesafeMap;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ModNetworkManager {

    private ModNetworkManager() {}

    private static final HashMap<Class<?>, ObjectRegister<?>> registerMap =  new HashMap<>();

    @Environment(EnvType.CLIENT)
    public static final HashMap<String, Class<?>> classMap = new HashMap<>();


    public static void register() {
        registerObject(IAbilityUser.class)
                .add(new ObjectRegisterEntry<>((u) -> u.getEntity().getUuid(), NetworkKeys.UUID, false, ObjectRegisterMethod.UUID))
                .add(new ObjectRegisterEntry<>((u) -> u.getEntity().getId(), NetworkKeys.ID, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>((u) -> u.getAbilities().size(), NetworkKeys.ABILITY_AMOUNT, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>(IAbilityUser::getMaxStamina, NetworkKeys.MAX_STAMINA, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>(IAbilityUser::getStamina, NetworkKeys.STAMINA, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>(IAbilityUser::isEnabled, NetworkKeys.ENABLED, true, ObjectRegisterMethod.BOOLEAN))
                .add(new ObjectRegisterEntry<>(IAbilityUser::isBlocked, NetworkKeys.BLOCKED, true, ObjectRegisterMethod.BOOLEAN));


        registerObject(IAbility.class)
                .add(new ObjectRegisterEntry<>((a) -> a.getUser().getEntity().getUuid(), NetworkKeys.UUID, false, ObjectRegisterMethod.UUID))
                .add(new ObjectRegisterEntry<>((a) -> a.getUser().getEntity().getId(), NetworkKeys.ID, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>(IAbility::getName, NetworkKeys.NAME, false, ObjectRegisterMethod.STRING))
                .add(new ObjectRegisterEntry<>(IAbility::getDescription, NetworkKeys.DESCRIPTION, false, ObjectRegisterMethod.STRING))
                .add(new ObjectRegisterEntry<>(IAbility::canExecute, NetworkKeys.CAN_EXECUTE, true, ObjectRegisterMethod.BOOLEAN));

        registerObject(IActivationAbility.class)
                .add(new ObjectRegisterEntry<>(IActivationAbility::isActive, NetworkKeys.IS_ACTIVE, true, ObjectRegisterMethod.BOOLEAN))
                .add(new ObjectRegisterEntry<>(IActivationAbility::getTicks, NetworkKeys.GET_TICKS, true, ObjectRegisterMethod.INT));

        registerObject(ICooldownAbility.class)
                .add(new ObjectRegisterEntry<>((a) -> a.getCooldown().getCooldownTicks(), NetworkKeys.COOLDOWN_TICKS, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>((a) -> a.getCooldown().getTicksRemaining(), NetworkKeys.COOLDOWN_TICKS_REMAINING, true, ObjectRegisterMethod.INT));

        registerObject(TogglePassiveAbility.class)
                .add(new ObjectRegisterEntry<>(TogglePassiveAbility::isActive, NetworkKeys.IS_ACTIVE, true, ObjectRegisterMethod.BOOLEAN));

        registerObject(EntityRenderAbility.class)
                .add(new ObjectRegisterEntry<>(EntityRenderAbility::getRange, NetworkKeys.RANGE, true, ObjectRegisterMethod.FLOAT));

        registerObject(LoadAmmoAbility.class)
                .add(new ObjectRegisterEntry<>((a) -> a.getAmmoAmount(LoadAmmoAbility.AmmoType.REGULAR), NetworkKeys.REGULAR_AMOUNT, true, ObjectRegisterMethod.INT))
                .add(new ObjectRegisterEntry<>((a) -> a.getAmmoAmount(LoadAmmoAbility.AmmoType.HOLLOW_POINT), NetworkKeys.HOLLOW_AMOUNT, true, ObjectRegisterMethod.INT));
    }

    @SuppressWarnings("unchecked")
    public static <OT> ObjectRegister<OT> registerObject(@NotNull Class<OT> objectType, boolean override) {
        if (override || !registerMap.containsKey(objectType))
            registerMap.put(objectType, new ObjectRegister<>(objectType));

        return (ObjectRegister<OT>) registerMap.get(objectType);
    }
    public static <OT> ObjectRegister<OT> registerObject(@NotNull Class<OT> abilityType) {
        return registerObject(abilityType, false);
    }

    public static void encode(@NotNull Object object, @NotNull PacketByteBuf buffer, boolean minimal) {
        Class<?> type = object.getClass();

        buffer.writeString(type.getName());
        encode(object, type, buffer, minimal, new HashSet<>());
    }
    private static void encode(Object object, @NotNull Class<?> type, @NotNull PacketByteBuf buffer, boolean minimal, @NotNull HashSet<Class<?>> encodedTypes) {
        if (!encodedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            encode(object, superType, buffer, minimal, encodedTypes);

        for (Class<?> interfaceType : type.getInterfaces())
            encode(object, interfaceType, buffer, minimal, encodedTypes);

        if (registerMap.containsKey(type))
            registerMap.get(type).tryEncode(object, buffer, minimal);
    }

    public static <T> @NotNull IReadonlyTypesafeMap decode(@NotNull PacketByteBuf buffer, boolean minimal) {
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

        map.put(NetworkKeys.TYPE, type);

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


    public record ObjectRegisterEntry<OT, ET>(Function<OT, ET> serverGetter, IReadonlyTypesafeMap.Key<ET> clientKey, boolean minimal, ObjectRegisterMethod<ET> registerMethod) {

        public void encode(@NotNull OT object, @NotNull PacketByteBuf buffer) {
            registerMethod().bufferEncoder().accept(buffer, serverGetter().apply(object));
        }

        public void decode(@NotNull PacketByteBuf buffer, @NotNull TypesafeMap map) {
            map.put(clientKey(), registerMethod().bufferDecoder().apply(buffer));
        }

    }
    public record ObjectRegisterMethod<ET>(BiConsumer<PacketByteBuf, ET> bufferEncoder, Function<PacketByteBuf, ET> bufferDecoder) {

        public static final ObjectRegisterMethod<Boolean> BOOLEAN = new ObjectRegisterMethod<>(PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

        public static final ObjectRegisterMethod<Integer> INT = new ObjectRegisterMethod<>(PacketByteBuf::writeInt, PacketByteBuf::readInt);
        public static final ObjectRegisterMethod<Long> LONG = new ObjectRegisterMethod<>(PacketByteBuf::writeLong, PacketByteBuf::readLong);

        public static final ObjectRegisterMethod<Float> FLOAT = new ObjectRegisterMethod<>(PacketByteBuf::writeFloat, PacketByteBuf::readFloat);
        public static final ObjectRegisterMethod<Double> DOUBLE = new ObjectRegisterMethod<>(PacketByteBuf::writeDouble, PacketByteBuf::readDouble);

        public static final ObjectRegisterMethod<String> STRING = new ObjectRegisterMethod<>(PacketByteBuf::writeString, PacketByteBuf::readString);

        public static final ObjectRegisterMethod<UUID> UUID = new ObjectRegisterMethod<>(PacketByteBuf::writeUuid, PacketByteBuf::readUuid);

    }


    public static final class ObjectRegister<OT> {

        private final Class<OT> objectType;
        private final LinkedHashSet<ObjectRegisterEntry<OT, ?>> entries;

        private ObjectRegister(@NotNull Class<OT> objectType) {
            this.objectType = objectType;
            this.entries = new LinkedHashSet<>();
        }

        @NotNull
        public Class<OT> getObjectType() {
            return objectType;
        }

        @NotNull
        public LinkedHashSet<ObjectRegisterEntry<OT, ?>> getEntries() {
            return entries;
        }

        @NotNull
        public <ET> ModNetworkManager.ObjectRegister<OT> add(@NotNull ModNetworkManager.ObjectRegisterEntry<OT, ET> entry) {
            entries.add(entry);
            return this;
        }

        public void encode(@NotNull OT object, @NotNull PacketByteBuf buffer, boolean minimal) {
            for (ObjectRegisterEntry<OT, ?> entry : entries) {
                //if (entry.minimal() || !minimal)
                    entry.encode(object, buffer);
            }
        }

        public void decode(@NotNull PacketByteBuf buffer, @NotNull TypesafeMap map, boolean minimal) {
            for (ObjectRegisterEntry<OT, ?> entry : entries) {
                //if (entry.minimal() || !minimal)
                    entry.decode(buffer, map);
            }
        }

        public void tryEncode(@NotNull Object object, @NotNull PacketByteBuf buffer, boolean minimal) {
            if (objectType.isAssignableFrom(object.getClass())) {
                OT castObject = objectType.cast(object);
                encode(castObject, buffer, minimal);
            }
        }

    }

}
