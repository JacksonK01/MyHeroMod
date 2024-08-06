package net.michaeljackson23.mineademia.abilitysystem.networking;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilityMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerAbilityUserPacketS2C {
    public static Identifier PLAYER_ABILITY_USER_PACKET = new Identifier(Mineademia.MOD_ID, "player_ability_user_packet");

    private final String[] abilities;
    private final String[] descriptions;
    private final int stamina;
    private final int maxStamina;
    private final int[] cooldowns;
    private final int[] maxCooldowns;
    private final UUID user;

    private PlayerAbilityUserPacketS2C(String[] abilities, String[] descriptions, int stamina, int maxStamina, int[] cooldowns, int[] maxCooldowns, UUID user) {
        this.abilities = abilities;
        this.descriptions = descriptions;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.cooldowns = cooldowns;
        this.maxCooldowns = maxCooldowns;
        this.user = user;
    }

    public static PacketByteBuf encode(@NotNull PlayerAbilityUser user) {
        String[] abilities;
        String[] descriptions;
        int stamina;
        int maxStamina;
        int[] cooldowns;
        int[] maxCooldowns;
        LinkedList<Integer> cooldownsList = new LinkedList<>();
        LinkedList<Integer> maxCooldownsList = new LinkedList<>();

        IAbilityMap abilityMap = user.getAbilities();
        int size = abilityMap.size();

        abilities = new String[size];
        descriptions = new String[size];

        AtomicInteger i = new AtomicInteger(0);

        user.getAbilities().forEach((key, type) -> {
            if(type instanceof ICooldownAbility cooldownAbility) {
                Cooldown cd = cooldownAbility.getCooldown();

                cooldownsList.add(cd.getTicksRemaining());
                maxCooldownsList.add(cd.getCooldownTicks());
            }
            abilities[i.get()] = type.getName();
            descriptions[i.getAndIncrement()] = type.getDescription();
        });

        stamina = user.getStamina();
        maxStamina = user.getMaxStamina();

        cooldowns = cooldownsList.stream().mapToInt(Integer::intValue).toArray();
        maxCooldowns = maxCooldownsList.stream().mapToInt(Integer::intValue).toArray();

        PacketByteBuf buf = PacketByteBufs.create();
        writeStringArray(buf, abilities);
        writeStringArray(buf, descriptions);
        buf.writeInt(stamina);
        buf.writeInt(maxStamina);
        buf.writeIntArray(cooldowns);
        buf.writeIntArray(maxCooldowns);
        buf.writeUuid(user.getEntity().getUuid());
        return buf;
    }

    public static PlayerAbilityUserPacketS2C decode(@NotNull PacketByteBuf buf) {
        return new PlayerAbilityUserPacketS2C(readStringArray(buf), readStringArray(buf), buf.readInt(), buf.readInt(), buf.readIntArray(), buf.readIntArray(), buf.readUuid());
    }

    public static void sendToClient(@NotNull PlayerAbilityUser user) {
        PacketByteBuf buf = encode(user);
        ServerPlayNetworking.send(user.getEntity(), PLAYER_ABILITY_USER_PACKET, buf);
    }

    //Sends AbilityUser data to every client. Useful for some cases.
    public static void sendProxy(@NotNull PlayerAbilityUser user) {
        PacketByteBuf buf = encode(user);
        ServerPlayerEntity serverPlayer = user.getEntity();
        for (ServerPlayerEntity otherPlayer : Objects.requireNonNull(serverPlayer.getServer()).getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(otherPlayer, PLAYER_ABILITY_USER_PACKET, buf);
        }
    }

    private static void writeStringArray(@NotNull PacketByteBuf buf, @NotNull String[] array) {
        //Need to write length for decoding purposes.
        buf.writeInt(array.length);
        for (String s : array) {
            buf.writeString(s);
        }
    }

    private static String[] readStringArray(@NotNull PacketByteBuf buf) {
        int length = buf.readInt();
        String[] array = new String[length];

        for(int i = 0; i < length; i++) {
            array[i] = buf.readString();
        }

        return array;
    }

    public String[] getAbilities() {
        return this.abilities;
    }

    public String[] getDescriptions() {
        return this.descriptions;
    }

    public int getStamina() {
        return this.stamina;
    }

    public int getMaxStamina() {
        return this.maxStamina;
    }

    public int[] getCooldowns() {
        return this.cooldowns;
    }

    public int[] getMaxCooldowns() {
        return this.maxCooldowns;
    }

    public UUID getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "[Abilities: " + Arrays.toString(abilities) + "] + [Stamina " + stamina + "]";
    }
}
