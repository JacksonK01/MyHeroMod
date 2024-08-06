package net.michaeljackson23.mineademia.abilitysystem.impl;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.passive.IEventPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IPlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.networking.PlayerAbilityUserPacketS2C;
import net.michaeljackson23.mineademia.abilitysystem.usage.AbilitySets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public final class AbilityManager {

    private AbilityManager() { }


    // private static final HashSet<IAbility> abilities = new HashSet<>();
    private static final HashMap<UUID, IAbilityUser> users = new HashMap<>();
    private static final HashMap<UUID, IPlayerAbilityUser> playerUsers = new HashMap<>();

    private static final HashMap<IAbilityUser, Integer> userStaminaRegen = new HashMap<>();

    private static final HashSet<ITickAbility> tickAbilities = new HashSet<>();
    private static final HashSet<ICooldownAbility> cooldownAbilities = new HashSet<>();
    private static final HashSet<IEventPassiveAbility<?>> eventAbilities = new HashSet<>();

    public static void registerAbility(@NotNull IAbility ability) {
        // abilities.add(ability);

        if (ability instanceof ITickAbility tickAbility)
            tickAbilities.add(tickAbility);
        if (ability instanceof ICooldownAbility cooldownAbility)
            cooldownAbilities.add(cooldownAbility);
        if (ability instanceof IEventPassiveAbility<?> eventAbility)
            eventAbilities.add(eventAbility);
    }

    public static void registerAbilities(@NotNull IAbilityUser user) {
        for (IAbility ability : user.getAbilities().values())
            registerAbility(ability);
    }

    public static void unregisterAbilities(@NotNull IAbilityUser user) {
        tickAbilities.removeIf((a) -> a.getUser().equals(user));
        cooldownAbilities.removeIf((a) -> a.getUser().equals(user));
        eventAbilities.removeIf((a) -> a.getUser().equals(user));
    }

    public static void onServerTick(MinecraftServer minecraftServer) {
        for (ITickAbility tickAbility : tickAbilities)
            tickAbility.onTick();
        for (ICooldownAbility cooldownAbility : cooldownAbilities)
            cooldownAbility.getCooldown().onTick();

        regenUserStamina();
        sendUserPacket();
    }

    public static void triggerEvents(Class<?> eventType) {
        for (IEventPassiveAbility<?> eventAbility : eventAbilities)
            eventAbility.tryTriggerEvent(eventType);
    }


    @SuppressWarnings("unchecked")
    public static void onPlayerJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();
        UUID uuid = player.getUuid();

        PlayerAbilityUser user = (PlayerAbilityUser) getUser(player);
        if (user == null) {
            user = new PlayerAbilityUser(player);
            user.setAbilities(AbilitySets.GENERAL);

            users.put(uuid, user);
            playerUsers.put(uuid, user);
            userStaminaRegen.put(user, 0); // not absent cause if you rejoin that SHOULD reset
        } else
            user.setEntity(player);

        PlayerAbilityUserPacketS2C.sendToClient(user);

        triggerEvents(ServerPlayConnectionEvents.Join.class);
    }

    @Nullable
    public static IPlayerAbilityUser getUser(@NotNull ServerPlayerEntity player) {
        return playerUsers.get(player.getUuid());
    }

    private static void regenUserStamina() {
        for (IAbilityUser user : users.values()) {
            int sinceLastRegen = userStaminaRegen.get(user);

            if (sinceLastRegen >= user.getStaminaRegenRate()) {
                if (user.onStaminaRegen()) {
                    user.offsetStamina(user.getStaminaRegenAmount());
                    userStaminaRegen.put(user, 0);
                    continue;
                }
            }

            userStaminaRegen.put(user, sinceLastRegen + 1);
        }
    }

    private static void sendUserPacket() {
        for (IPlayerAbilityUser user : playerUsers.values()) {
            PlayerAbilityUserPacketS2C.sendToClient((PlayerAbilityUser) user);
        }
    }
}
