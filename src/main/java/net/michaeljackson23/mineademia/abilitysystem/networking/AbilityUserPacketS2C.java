package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public final class AbilityUserPacketS2C {

    public static final Identifier S2C_ABILITY_USER_PACKET = new Identifier(Mineademia.MOD_ID, "ability_user_packet");


    private final UUID uuid;

    private final int maxStamina;
    private final int stamina;

    private final boolean enabled;
    private final boolean blocked;

    private final HashMap<Class<?>, IReadonlyTypesafeMap> abilityMap;

    private AbilityUserPacketS2C(UUID uuid, int maxStamina, int stamina, boolean enabled, boolean blocked, HashMap<Class<?>, IReadonlyTypesafeMap> abilityMap) {
        this.uuid = uuid;
        this.maxStamina = maxStamina;
        this.stamina = stamina;
        this.enabled = enabled;
        this.blocked = blocked;
        this.abilityMap = abilityMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public int getStamina() {
        return stamina;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public HashMap<Class<?>, IReadonlyTypesafeMap> getAbilityMap() {
        return abilityMap;
    }


    public static @NotNull PacketByteBuf encode(@NotNull IAbilityUser user, boolean minimal) {
        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeBoolean(minimal);
        encodeUser(user, buffer);

        for (IAbility ability : user.getAbilities().values())
            AbilityNetworkManager.encode(ability, buffer, minimal);

        return buffer;
    }

    public static @NotNull AbilityUserPacketS2C decode(@NotNull PacketByteBuf buffer) {
        boolean minimal = buffer.readBoolean();

        UUID uuid = buffer.readUuid();

        int abilityAmount = buffer.readInt();

        int maxStamina = buffer.readInt();
        int stamina = buffer.readInt();

        boolean enabled = buffer.readBoolean();
        boolean blocked = buffer.readBoolean();

        HashMap<Class<?>, IReadonlyTypesafeMap> abilityMap = new HashMap<>();

        for (int i = 0; i < abilityAmount; i++) {
            IReadonlyTypesafeMap ability = AbilityNetworkManager.decode(buffer, minimal);
            abilityMap.put(ability.get(AbilityKeys.TYPE), ability);
        }

        return new AbilityUserPacketS2C(uuid, maxStamina, stamina, enabled, blocked, abilityMap);
    }

    private static void encodeUser(@NotNull IAbilityUser user, @NotNull PacketByteBuf buffer) {
        buffer.writeUuid(user.getEntity().getUuid());

        buffer.writeInt(user.getAbilities().size());

        buffer.writeInt(user.getMaxStamina());
        buffer.writeInt(user.getStamina());

        buffer.writeBoolean(user.isEnabled());
        buffer.writeBoolean(user.isBlocked());
    }

    public static void sendSelf(@NotNull ServerPlayerEntity player, @NotNull IAbilityUser user, boolean minimal) {
        ServerPlayNetworking.send(player, S2C_ABILITY_USER_PACKET, encode(user, minimal));
    }

}
