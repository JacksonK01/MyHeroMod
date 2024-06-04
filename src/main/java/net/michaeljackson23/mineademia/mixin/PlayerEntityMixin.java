package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirks.Quirkless;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements QuirkDataHelper {
    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Unique
    private QuirkData quirkData = new QuirkData();
    @Unique
    private Quirk quirk = new Quirkless();

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        quirkData.writeNbt(nbt);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        quirkData.readNbt(nbt);
    }

    @Unique
    public QuirkData myHeroMod$getQuirkData() {
        if(this.quirkData != null) {
            return this.quirkData;
        }

        return new QuirkData();
    }
    @Unique
    public void myHeroMod$setQuirkData(QuirkData data) {
        this.quirkData = data;
    }
    //Intentionally only callable for ServerPlayerEntities
    @Unique
    public Quirk myHeroMod$getQuirk(MinecraftServer server) {
        if(server == null) {
            return null;
        }
        return this.quirk;
    }

    @Unique
    public void myHeroMod$setQuirk(MinecraftServer server, Quirk quirk) {
        if(server == null) {
            return;
        }
        this.quirk = quirk;
    }
}
