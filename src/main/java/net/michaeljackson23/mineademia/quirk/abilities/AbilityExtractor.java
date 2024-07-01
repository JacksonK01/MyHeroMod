package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public interface AbilityExtractor {
    AbilityBase extract(ServerPlayerEntity player, Quirk quirk);
}
