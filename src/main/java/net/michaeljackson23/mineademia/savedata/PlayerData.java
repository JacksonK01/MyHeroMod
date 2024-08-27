package net.michaeljackson23.mineademia.savedata;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.combo.ComboManager;
import net.michaeljackson23.mineademia.mixin.OldServerPlayerEntityMixin;
import net.michaeljackson23.mineademia.networking.PacketsC2S;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.quirks.NullQuirk;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

//This may feel like useless abstraction but this is built to properly scale with size of the mod as it grows
/**
 * <p>
 *     This is the object that gets stored on the player
 * </p>
 * <p>
 *     After reading this, check out {@link net.michaeljackson23.mineademia.quirk.Quirk}
 * </p>
 */
public class PlayerData {

    /**
     * <p>
     *     This isn't super important. It's only used for directing {@link net.michaeljackson23.mineademia.quirk.QuirkInitialize}
     *     which quirk to give to the player. After that it's used for reading and writing saved data to the disk.
     * </p>
     */
    QuirkBuilder builder = new QuirkBuilder("", true);

    /**
     * <p>
     *     This is important. It's set to {@link NullQuirk} on purpose to signal that
     *     this playerdata doesn't have a quirk yet. It also helps prevent null pointer exceptions,
     *     just in case any threads get out of sync.
     * </p>
     */
    private Quirk quirk = new NullQuirk();

    private ComboManager comboManager = new ComboManager();

    /**
     * <p>
     *     Ignore this
     * </p>
     */
    private int dodgeCounter;

    /**
     * <p>
     *     Ignore this
     * </p>
     */
    private int blockingCounter;

    /**
     * <p>
     *     Ignore this
     * </p>
     */
    private ArrayList<String> obtainedQuirks = new ArrayList<>();

    public void tick(ServerPlayerEntity player) {
        comboManager.tick(player);
        quirk.tick(player);
    }

    /**
     * <p>
     *     This method helps {@link StateSaverAndLoader#createFromNbt(NbtCompound)} read the nbt data
     * </p>
     * <p>
     *     Used by = {@link StateSaverAndLoader#createFromNbt(NbtCompound)}
     * </p>
     */
    public void readNbt(NbtCompound nbt) {
        boolean isRandom = nbt.getBoolean("ShouldQuirkBeRandom");
        String quirk = nbt.getString(Mineademia.MOD_ID + ".quirkName");
        //This reBuild method is just basically resetting the data in builder
        builder.reBuild(quirk, isRandom);
    }

    /**
     * <p>
     *     This method helps {@link StateSaverAndLoader#writeNbt(NbtCompound)} write the nbt data
     * </p>
     * <p>
     *     Used by = {@link StateSaverAndLoader#writeNbt(NbtCompound)}
     * </p>
     */
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("ShouldQuirkBeRandom", builder.isRandom());
        nbt.putString(Mineademia.MOD_ID + ".quirkName", quirk.getName());
    }

    /**
     * <p>
     *     Sets the quirk for this player's data. NOTE: This is only ever used in the mixin, ignore all mixins for now.
     * </p>
     * <p>
     *     Used by = {@link OldServerPlayerEntityMixin}
     * </p>
     */
    public void setQuirk(Quirk quirk) {
        if(!this.obtainedQuirks.contains(quirk.getName()) && !(quirk instanceof NullQuirk)) {
            obtainedQuirks.add(quirk.getName());
        }
        this.quirk = quirk;
    }

    /**
     * <p>
     *     Returns the quirk for this player's data. Ignore mixins.
     * </p>
     * <p>
     *     Used by = {@link OldServerPlayerEntityMixin},
     *     {@link PacketsC2S#activateAbility(ServerPlayerEntity, PacketByteBuf, int)},
     *     {@link net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket#send(ServerPlayerEntity)},
     *     {@link net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket#sendProxy(ServerPlayerEntity)}
     * </p>
     */
    public Quirk getQuirk() {
        return this.quirk;
    }

    /**
     * <p>
     *     Ignore this
     * </p>
     */
    public ArrayList<String> getObtainedQuirksList() {
        return obtainedQuirks;
    }

    /**
     * <p>
     *     Ignore this
     * </p>
     */
    public QuirkBuilder getQuirkBuilder() {
        return this.builder;
    }

    public ComboManager getComboManager() {
        return this.comboManager;
    }
}
