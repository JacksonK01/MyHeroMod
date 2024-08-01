package net.michaeljackson23.mineademia.abilitiestest.impl;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.abilitiestest.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
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

    public static void unregisterAbilities(@NotNull IAbilityUser user, boolean onlyContains) {
        tickAbilities.removeIf((a) -> a.getUser().equals(user) && (!onlyContains || user.getAbilities().contains(a)));
        cooldownAbilities.removeIf((a) -> a.getUser().equals(user) && (!onlyContains || user.getAbilities().contains(a)));
    }
    public static void unregisterAbilities(@NotNull IAbilityUser user) {
        unregisterAbilities(user, true);
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
        if (user == null)
            user = new PlayerAbilityUser(player);

        playerUsers.putIfAbsent(player.getUuid(), user);

        user.setAbilities(AbilitySets.GENERAL);
    }

    @Nullable
    public static PlayerAbilityUser getUser(@NotNull ServerPlayerEntity player) {
        return playerUsers.get(player.getUuid());
    }

}
