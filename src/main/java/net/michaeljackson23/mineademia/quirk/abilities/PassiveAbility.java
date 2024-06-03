package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public interface PassiveAbility {
    //will return true when ability is done, false while is still active
    //true = ability is done
    boolean isDone(ServerPlayerEntity player, Quirk quirk);
}
