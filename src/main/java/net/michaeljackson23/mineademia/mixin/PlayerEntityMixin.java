package net.michaeljackson23.mineademia.mixin;

import com.mojang.authlib.GameProfile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.michaeljackson23.mineademia.quirk.quirks.Quirkless;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.UUID;
//TODO undo this. Make this a persistent state
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements QuirkDataHelper {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow public abstract Text getName();

    @Shadow public abstract Text getDisplayName();

    @Unique
    private static HashMap<UUID, NbtCompound> tempData = new HashMap<>();
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

    @Inject(at = @At("TAIL"), method = "onDeath")
    private void onDeath(DamageSource source, CallbackInfo ci) {
        NbtCompound nbt = new NbtCompound();
        quirkData.writeNbt(nbt);
        tempData.put(getUuid(), nbt);
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
    //Intentionally only callable serverside
    @Unique
    public Quirk myHeroMod$getQuirk(MinecraftServer server) {
        if(server == null) {
            return null;
        }
        return this.quirk;
    }
    //Intentionally only callable serverside
    @Unique
    public void myHeroMod$setQuirk(MinecraftServer server, Quirk quirk) {
        if(server == null) {
            return;
        }
        this.quirk = quirk;
    }
}
