package net.michaeljackson23.mineademia.abilitysystem.networking;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public final class AbilityNetworkManager {

    private AbilityNetworkManager() {}


    private interface IAbilityEncoder<T extends IAbility> {

        void encode(@NotNull T ability, @NotNull PacketByteBuf buffer);

    }

    private interface IAbilityDecoder<T extends IAbility> {

        void decode();

    }

}
