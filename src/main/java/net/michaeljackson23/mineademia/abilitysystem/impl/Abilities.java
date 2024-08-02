package net.michaeljackson23.mineademia.abilitysystem.impl;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.AbilitySets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public final class Abilities {

    private Abilities() { }


    // private static final HashSet<IAbility> abilities = new HashSet<>();
    private static final HashMap<UUID, PlayerAbilityUser> playerUsers = new HashMap<>();

    private static final HashSet<ITickAbility> tickAbilities = new HashSet<>();
    private static final HashSet<ICooldownAbility> cooldownAbilities = new HashSet<>();

    public static void registerAbility(@NotNull IAbility ability) {
        // abilities.add(ability);

        if (ability instanceof ITickAbility tickAbility)
            tickAbilities.add(tickAbility);
        if (ability instanceof ICooldownAbility cooldownAbility)
            cooldownAbilities.add(cooldownAbility);
    }

    public static void registerAbilities(@NotNull IAbilityUser user) {
        for (IAbility ability : user.getAbilities().values())
            registerAbility(ability);
    }

    public static void unregisterAbilities(@NotNull IAbilityUser user) {
        tickAbilities.removeIf((a) -> a.getUser().equals(user));
        cooldownAbilities.removeIf((a) -> a.getUser().equals(user));
    }

    public static void tickAbilities(MinecraftServer minecraftServer) {
        for (ITickAbility tickAbility : tickAbilities)
            tickAbility.onTick();
        for (ICooldownAbility cooldownAbility : cooldownAbilities)
            cooldownAbility.getCooldown().onTick();
    }


    @SuppressWarnings("unchecked")
    public static void initAbilityUser(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.getPlayer();

        PlayerAbilityUser user = getUser(player);
        if (user == null) {
            user = new PlayerAbilityUser(player);
            user.setAbilities(AbilitySets.GENERAL);
        }

        playerUsers.putIfAbsent(player.getUuid(), user);
    }

    @Nullable
    public static PlayerAbilityUser getUser(@NotNull ServerPlayerEntity player) {
        return playerUsers.get(player.getUuid());
    }

}
