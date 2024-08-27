package net.michaeljackson23.mineademia.mixin.common;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.savedata.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class OldServerPlayerEntityMixin implements QuirkAccessor, PlayerDataAccessor {
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
