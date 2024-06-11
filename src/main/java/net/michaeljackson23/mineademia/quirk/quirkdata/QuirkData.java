package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.Arrays;
@Environment(value= EnvType.CLIENT)
public class QuirkData {
    private String quirkName = "";
    private double stamina = 1000;
    private int cooldown = 0;
    private String[] models = new String[0];
    private String activeAbility = "";

    public QuirkData() {}

    public QuirkData(String quirkName, String[] models, double stamina, int cooldown, String activeAbility) {
        this.quirkName = quirkName;
        this.models = models;
        this.stamina = stamina;
        this.cooldown = cooldown;
        this.activeAbility = activeAbility;
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

    public String getActiveAbility() {
        return this.activeAbility;
    }

    @Override
    public String toString() {
        return "Quirk: {" + quirkName + "} Models: {" + Arrays.toString(models) + "} Stamina: {" + stamina + "} Cooldown: {" + cooldown + "}";
    }
}
