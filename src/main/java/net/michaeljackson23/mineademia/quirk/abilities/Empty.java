package net.michaeljackson23.mineademia.quirk.abilities;


import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public class Empty extends AbilityBase {
    public Empty() {
        super(0, 0, 0, false, "empty", "no ability");
    }

    @Override
    public void activate(ServerPlayerEntity player, Quirk quirk) {

    }
}
