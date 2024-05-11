package net.michaeljackson23.mineademia.init;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.IAbilityHandler;

import java.util.*;

public class PlayerData {
    //This is all the information I store onto the player
    private String quirk = "empty";
    //Cap is 5 abilities for now.
    private AbilityBase[] abilities = new AbilityBase[5];
    private int cooldown = 0;
    private int stamina = 1000;
    //This is for leveling up your quirk, I don't know what I'll use it for yet
    private List<Integer> quirkStats = new ArrayList<>();
    private Queue<AbilityBase> abilityQueue = new LinkedList<>();

    public PlayerData() {

    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        if (cooldown >= 0) {
            this.cooldown = cooldown;
        }
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        if (stamina >= 0) {
            this.stamina = stamina;
        }
    }

    public String getQuirk() {
        return this.quirk;
    }

    public void setQuirk(String quirk) {
        this.quirk = quirk;
    }

    public AbilityBase[] getAbilities() {
        return abilities;
    }

    public List<Integer> getQuirkStats() {
        return quirkStats;
    }

    public void setAbility(int index, AbilityBase ability) {
        if (index >= 0 && index < abilities.length) {
            abilities[index] = ability;
        }
    }

    public void setAbilities(AbilityBase[] abilities) {
        this.abilities = abilities;
    }

    public Queue<AbilityBase> getAbilityQueue() {
        return abilityQueue;
    }

}
