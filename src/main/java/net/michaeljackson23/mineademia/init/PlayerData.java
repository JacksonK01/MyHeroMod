package net.michaeljackson23.mineademia.init;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.PassiveAbility;

import java.util.*;

public class PlayerData {
    //This is all the information I store onto the player
    private String quirk = "empty";
    //Cap is 5 abilities for now. The order of the array corresponds to the keybinds, like ability one keybind is index 0 and ability 3 is index 4 etc etc...
    private AbilityBase[] abilities = new AbilityBase[5];
    //This is the ability that will be activated and used in for the server ticks
    private AbilityBase activeAbility;
    private int cooldown = 0;
    private int stamina = 1000;
    //This is for leveling up your quirk, I don't know what I'll use it for yet
    private ArrayList<Integer> quirkStats = new ArrayList<>();
    //Bring this back some time
    //private Queue<AbilityBase> abilityQueue = new LinkedList<>();
    private LinkedList<PassiveAbility> passiveAbilities = new LinkedList<>();

    public PlayerData() {

    }

    public AbilityBase getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(AbilityBase activeAbility) {
        this.activeAbility = activeAbility;
    }

    public LinkedList<PassiveAbility> getPassiveAbilities() {
        return this.passiveAbilities;
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

//    public Queue<AbilityBase> getAbilityQueue() {
//        return abilityQueue;
//    }

}
