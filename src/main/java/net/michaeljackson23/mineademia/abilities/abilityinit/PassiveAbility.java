package net.michaeljackson23.mineademia.abilities.abilityinit;

import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PassiveAbility {
    //will return true when ability is done, false while is still active
    //true = ability is done
    boolean activate();
}
