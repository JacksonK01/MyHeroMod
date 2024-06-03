package net.michaeljackson23.mineademia.quirk.quirks;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.Griddy;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class Electrification extends Quirk {
    private boolean init;

    public Electrification() {
        super("Electrification", new Griddy(), new Empty(), new Empty(), new Empty(), new Empty());
    }

    @Override
    protected void init(ServerPlayerEntity player) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeString("engines");
        ServerPlayNetworking.send(player, Networking.LOAD_QUIRK_FEATURE, data);
    }
}
