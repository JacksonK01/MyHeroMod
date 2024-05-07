package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.QuirkAbilities;
import net.michaeljackson23.mineademia.abilities.ofa.Air_Force;
import net.michaeljackson23.mineademia.abilities.Empty;
import net.michaeljackson23.mineademia.abilities.ofa.Blackwhip;
import net.michaeljackson23.mineademia.abilities.ofa.Cowling;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class AbilityMap {
    public HashMap<Integer, AbilityBase> abilityHandlerMap = new HashMap<>();
    //TODO THIS CAN BE CLONED NOW

    public AbilityMap(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
        abilityHandlerMap.put(QuirkAbilities.EMPTY.getValue(), new Empty(player, playerData, server, slot));
        abilityHandlerMap.put(QuirkAbilities.AIR_FORCE.getValue(), new Air_Force(player, playerData, server, slot));
//        abilityHandlerMap.put(QuirkAbilities.BLACKWHIP.getValue(), new Blackwhip(player, playerData, server, slot));
//        abilityHandlerMap.put(QuirkAbilities.COWLING.getValue(), new Cowling(player, playerData, server, slot));
    }
}
