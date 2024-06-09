package net.michaeljackson23.mineademia.savedata;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.quirks.NullQuirk;
import net.michaeljackson23.mineademia.quirk.quirks.Quirkless;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;

//This may feel like useless abstraction but this is built to properly scale with size of the mod as it grows
public class PlayerData {
    QuirkBuilder builder = new QuirkBuilder("", true);
    //An empty quirk just for init.
    private Quirk quirk = new NullQuirk();
    private int dodgeCounter;
    private int blockingCounter;
    private ArrayList<String> obtainedQuirks = new ArrayList<>();

    public void readNbt(NbtCompound nbt) {
        boolean isRandom = nbt.getBoolean("ShouldQuirkBeRandom");
        String quirk = nbt.getString(Mineademia.MOD_ID + ".quirkName");
        builder.reBuild(quirk, isRandom);
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("ShouldQuirkBeRandom", builder.isRandom());
        nbt.putString(Mineademia.MOD_ID + ".quirkName", quirk.getName());
    }

    public void setQuirk(Quirk quirk) {
        if(!this.obtainedQuirks.contains(quirk.getName()) && !(quirk instanceof NullQuirk)) {
            obtainedQuirks.add(quirk.getName());
        }
        this.quirk = quirk;
    }

    public Quirk getQuirk() {
        return this.quirk;
    }

    public ArrayList<String> getObtainedQuirksList() {
        return obtainedQuirks;
    }

    public void setQuirkBuilder() {

    }

    public QuirkBuilder getQuirkBuilder() {
        return this.builder;
    }
}
