package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.michaeljackson23.mineademia.abilitysystem.impl.server.ability.passive.TogglePassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.active.IActivationAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;

// @Environment(EnvType.SERVER)
public final class AbilityEncoders {

    private AbilityEncoders() { }


    public static final HashMap<String, Class<?>> classMap = new HashMap<>();
    public static final HashMap<Class<? extends IAbility>, BiConsumer<IAbility, PacketByteBuf>> encoderMap = new HashMap<>();


    public static void registerEncoders() {
        registerEncoder(IAbility.class, AbilityEncoders::encodeAbility);
        registerEncoder(IActiveAbility.class, AbilityEncoders::encodeActiveAbility);
        registerEncoder(IActivationAbility.class, AbilityEncoders::encodeActivationAbility);

        registerEncoder(ICooldownAbility.class, AbilityEncoders::encodeCooldownAbility);

        registerEncoder(TogglePassiveAbility.class, AbilityEncoders::encodePassiveToggleAbility);
    }
    private static void registerEncoder(@NotNull Class<? extends IAbility> type, @NotNull BiConsumer<IAbility, PacketByteBuf> encoder) {
        classMap.putIfAbsent(type.getName(), type);
        encoderMap.put(type, encoder);
    }


    public static void encode(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        Class<?> type = ability.getClass();
        classMap.putIfAbsent(type.getName(), ability.getClass());

        buffer.writeString(type.getName());
        encode(ability, type, buffer, new HashSet<>());
    }
    private static void encode(@NotNull IAbility ability, @NotNull Class<?> type, @NotNull PacketByteBuf buffer, @NotNull HashSet<Class<?>> encodedTypes) {
        if (!encodedTypes.add(type))
            return;

        Class<?> superType = type.getSuperclass();
        if (superType != null && !superType.equals(Object.class))
            encode(ability, superType, buffer, encodedTypes);

        for (Class<?> interfaceType : type.getInterfaces())
            encode(ability, interfaceType, buffer, encodedTypes);

        if (encoderMap.containsKey(type))
            encoderMap.get(type).accept(ability, buffer);
    }


    private static void encodeAbility(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        buffer.writeString(ability.getName());
        buffer.writeString(ability.getDescription());
        buffer.writeBoolean(ability.canExecute());
    }

    private static void encodeActiveAbility(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        if (!(ability instanceof IActiveAbility activeAbility))
            return;

//        buffer.writeIntArray(activeAbility.getCategories().stream().mapToInt(Enum::ordinal).toArray());
//        buffer.writeIntArray(activeAbility.getBlockedCategories().stream().mapToInt(Enum::ordinal).toArray());
    }

    private static void encodeActivationAbility(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        if (!(ability instanceof IActivationAbility activationAbility))
            return;

        buffer.writeBoolean(activationAbility.isActive());
        buffer.writeInt(activationAbility.getTicks());
    }

    private static void encodeCooldownAbility(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        if (!(ability instanceof ICooldownAbility cooldownAbility))
            return;

        buffer.writeInt(cooldownAbility.getCooldown().getCooldownTicks());
        buffer.writeInt(cooldownAbility.getCooldown().getTicksRemaining());
    }

    private static void encodePassiveToggleAbility(@NotNull IAbility ability, @NotNull PacketByteBuf buffer) {
        if (!(ability instanceof TogglePassiveAbility togglePassiveAbility))
            return;

        buffer.writeBoolean(togglePassiveAbility.isToggled());
    }

}
