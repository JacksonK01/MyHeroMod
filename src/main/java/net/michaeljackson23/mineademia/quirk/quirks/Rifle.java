package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.minecraft.server.network.ServerPlayerEntity;

public class Rifle extends Quirk {
    public Rifle() {
        super("Rifle", new Empty(), new Empty(), new Empty(), new Empty(), new Empty());
    }

    public void tick(ServerPlayerEntity player) {
        super.tick(player);
        if(getActiveAbility().isActive()) {
            addModel("Sniper");
        }
    }
}
