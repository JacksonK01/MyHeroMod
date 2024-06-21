package net.michaeljackson23.mineademia.quirk.abilities;


import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public class Empty extends BasicAbility {
    public Empty() {
        super(0, 0, 0, "Empty Slot", "Fill this slot");
    }

    @Override
    public void activate(ServerPlayerEntity player, Quirk quirk) {

    }
}
