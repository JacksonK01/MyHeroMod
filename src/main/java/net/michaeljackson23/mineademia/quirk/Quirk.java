package net.michaeljackson23.mineademia.quirk;

import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Quirk {
    private String name;
    //Cap is 5 abilities for now. The order of the array corresponds to the keybinds, like ability one keybind is index 0 and ability 3 is index 4 etc etc...
    private AbilityBase[] abilities = new AbilityBase[5];
    //This is the ability that will be activated and used in for the server ticks
    private AbilityBase activeAbility;
    private int cooldown = 0;
    private double stamina = 1000;
    //This is for leveling up your quirk, I don't know what I'll use it for yet
    private ArrayList<Integer> quirkStats = new ArrayList<>();
    private LinkedList<PassiveAbility> passives = new LinkedList<>();

    public Quirk(String name, AbilityBase one, AbilityBase two, AbilityBase three, AbilityBase four, AbilityBase five){
        this.name = name;
        this.abilities[0] = one;
        this.abilities[1] = two;
        this.abilities[2] = three;
        this.abilities[3] = four;
        this.abilities[4] = five;
    }

    public void tick(ServerPlayerEntity player) {
        if (activeAbility != null) {
            activeAbility.execute(player, this);
            if (!activeAbility.isActive()) {
                activeAbility = null;
            }
        }

        Iterator<PassiveAbility> passiveAbilityIterator = passives.iterator();
        while (passiveAbilityIterator.hasNext()) {
            PassiveAbility next = passiveAbilityIterator.next();
            if(next.activate()) {
                passiveAbilityIterator.remove();
                player.sendMessage(Text.literal("Removed Passive"));
            }
        }

        if (stamina < 1000) {
            stamina++;
        }

        if (cooldown > 0) {
            cooldown--;
        }
    }

    public AbilityBase getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(AbilityBase activeAbility) {
        this.activeAbility = activeAbility;
    }

    public LinkedList<PassiveAbility> getPassives() {
        return this.passives;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        if (cooldown >= 0) {
            this.cooldown = cooldown;
        }
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        if (stamina >= 0) {
            this.stamina = stamina;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
