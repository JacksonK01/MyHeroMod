package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.quirk.abilities.InfiniteAbility;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.Blackwhip;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class PickVestigeAbility extends InfiniteAbility {
    private final Blackwhip blackwhip = new Blackwhip();

    private Optional<AbilityBase> activeAbility = Optional.empty();

    public PickVestigeAbility() {
        super(0, 0, "Vestiges", "Hold shift while using this ability");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.VESTIGE_GUI, PacketByteBufs.empty());
        player.sendMessage(Text.literal(this.title));
        deactivate(player, quirk);
    }
}
