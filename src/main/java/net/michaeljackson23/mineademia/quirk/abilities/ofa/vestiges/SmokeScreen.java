package net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SmokeScreen extends BasicAbility {

    public SmokeScreen() {
        super(100, 2, 10, "Smokescreen", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        player.sendMessage(Text.literal(this.title + " Pressed: " + this.getAmountOfTimesActivated()));

    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        player.sendMessage(Text.literal("deActivate"));
    }
}
