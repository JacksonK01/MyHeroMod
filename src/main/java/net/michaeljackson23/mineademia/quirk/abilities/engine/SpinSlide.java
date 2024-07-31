package net.michaeljackson23.mineademia.quirk.abilities.engine;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpinSlide extends BasicAbility {
    protected SpinSlide() {
        super(1, 80, 40, "Spin Slide", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {

    }
}
