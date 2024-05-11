package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.QuirkAbilities;
import net.michaeljackson23.mineademia.abilities.ofa.AirForce;
import net.michaeljackson23.mineademia.abilities.Empty;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

//This is bad, i didn't really know what I was doing when I made this system.
public class AbilityMap {
    public HashMap<Integer, AbilityBase> abilityHandlerMap = new HashMap<>();
    //TODO THIS CAN BE CLONED NOW

    public AbilityMap(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
//        abilityHandlerMap.put(QuirkAbilities.EMPTY.getValue(), new Empty(player, playerData, server, slot));
//        abilityHandlerMap.put(QuirkAbilities.AIR_FORCE.getValue(), new AirForce(player, playerData, server, slot));
//        abilityHandlerMap.put(QuirkAbilities.BLACKWHIP.getValue(), new Blackwhip(player, playerData, server, slot));
//        abilityHandlerMap.put(QuirkAbilities.COWLING.getValue(), new Cowling(player, playerData, server, slot));
    }
}
