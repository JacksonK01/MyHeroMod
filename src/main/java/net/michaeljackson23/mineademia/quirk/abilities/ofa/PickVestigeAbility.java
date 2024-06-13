package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.Blackwhip;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PickVestigeAbility extends AbilityBase {
    Blackwhip blackwhip = new Blackwhip();

    AbilityBase selectedAbility = null;

    public PickVestigeAbility() {
        super(true, 0, 0, false, "Vestiges", "Hold shift while using this ability");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.VESTIGE_GUI, PacketByteBufs.empty());
        player.sendMessage(Text.literal(this.title));
        deactivate(player, quirk);
    }
}
