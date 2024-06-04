package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.Arrays;

public class QuirkData {
    private String quirkName = "";
    private double stamina = 1000;
    private int cooldown = 0;
    private String[] models = new String[0];

    public QuirkData() {}

    public QuirkData(String quirkName, String[] models, double stamina, int cooldown) {
        this.quirkName = quirkName;
        this.models = models;
        this.stamina = stamina;
        this.cooldown = cooldown;
    }

    public void readNbt(NbtCompound nbt) {
        if(nbt.contains(Mineademia.MOD_ID + ".quirkName")) {
            setQuirkName(nbt.getString(Mineademia.MOD_ID + ".quirkName"));
            ArrayList<String> modelsList = new ArrayList<>();
            for(int i = 0; i < nbt.getInt(Mineademia.MOD_ID + ".modelsCount"); i++) {
                modelsList.add(nbt.getString(Mineademia.MOD_ID + ".quirkModel." + i));
            }
            setModels(modelsList.toArray(new String[0]));
        }
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putString(Mineademia.MOD_ID + ".quirkName", quirkName);
        nbt.putInt(Mineademia.MOD_ID + ".modelsCount", getModelsArrayLength());
        for(int i = 0; i < getModelsArrayLength(); i++) {
            nbt.putString(Mineademia.MOD_ID + ".quirkModel." + i, getModel(i));
        }
    }

    public void buildFromQuirk(Quirk quirk) {
        this.quirkName = quirk.getName();
        this.models = quirk.getModelsForQuirk();
    }

    public void syncStaminaAndCooldown(Quirk quirk) {
        this.stamina = quirk.getStamina();
        this.cooldown = quirk.getCooldown();
    }

    public String getQuirkName() {
        return this.quirkName;
    }

    public void setQuirkName(String quirkName) {
        this.quirkName = quirkName;
    }

    public String getModel(int i) {
        if(0 > i || i >= models.length) {
            return "empty";
        }
        return models[i];
    }

    public void setModels(String... models) {
        this.models = models;
    }

    public int getModelsArrayLength() {
        return models.length;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    //TODO make this printable
    @Override
    public String toString() {
        return "Quirk: {" + quirkName + "} Models: {" + Arrays.toString(models) + "} Stamina: {" + stamina + "} Cooldown: {" + cooldown + "}";
    }
}
