package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public final class AbilityUserPacketS2C {

    public static PacketByteBuf encode(@NotNull IAbilityUser user) {
        PacketByteBuf buffer = PacketByteBufs.create();
        user.encode(buffer);

        return buffer;
    }

}
