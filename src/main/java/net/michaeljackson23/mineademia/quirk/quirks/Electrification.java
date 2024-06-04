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

    public Electrification() {
        super("Electrification", new Griddy(), new Empty(), new Empty(), new Empty(), new Empty());
        setModelsForQuirk("EnginesAndEngineFire");
    }
}
