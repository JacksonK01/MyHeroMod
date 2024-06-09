package net.michaeljackson23.mineademia.mixin;

import com.mojang.authlib.GameProfile;
import net.michaeljackson23.mineademia.callbacks.OnServerPlayerCreationCallback;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;
import net.michaeljackson23.mineademia.quirk.quirks.NullQuirk;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements QuirkAccessor, PlayerDataAccessor {
    @Unique
    private PlayerData playerData = new PlayerData();

    @Unique
    @Override
    public Quirk myHeroMod$getQuirk() {
        return playerData.getQuirk();
    }

    @Unique
    @Override
    public void myHeroMod$setQuirk(Quirk quirk) {
        this.playerData.setQuirk(quirk);
    }

    @Unique
    @Override
    public PlayerData myHeroMod$getPlayerData() {
        return this.playerData;
    }

    @Unique
    @Override
    public void myHeroMod$setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
}
