package net.michaeljackson23.mineademia.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.networking.PlayerAbilityUserPacketS2C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(value= EnvType.CLIENT)
public class ClientCache {
    private static PlayerAbilityUserPacketS2C playerAbilityUserData;

    public static void setData(@NotNull PlayerAbilityUserPacketS2C toAdd) {
        playerAbilityUserData = toAdd;
    }

    @Nullable
    public static PlayerAbilityUserPacketS2C get() {
        return playerAbilityUserData;
    }
}
