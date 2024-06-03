package net.michaeljackson23.mineademia.quirk;

import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public abstract class Quirk {
    private String name;
    //Cap is 5 abilities for now. The order of the array corresponds to the keybinds, like ability one keybind is index 0 and ability 3 is index 4 etc etc...
    private AbilityBase[] abilities = new AbilityBase[5];
    //This is the ability that will be activated and used in for the server ticks
    private AbilityBase activeAbility;
    private int cooldown = 0;
    private double stamina = 1000;
    private boolean hasInit = false;
    private boolean deActivate = false;
    //This is for leveling up your quirk, I don't know what I'll use it for yet
    private ArrayList<Integer> quirkStats = new ArrayList<>();
    private LinkedList<PassiveAbility> passives = new LinkedList<>();

    public Quirk(String name, AbilityBase one, AbilityBase two, AbilityBase three, AbilityBase four, AbilityBase five) {
        this.name = name;
        this.abilities[0] = one;
        this.abilities[1] = two;
        this.abilities[2] = three;
        this.abilities[3] = four;
        this.abilities[4] = five;
    }

    public void tick(ServerPlayerEntity player) {
        processInit(player);
        handleActiveAbility(player);
        processPassives(player);
        regenerateStamina();
        reduceCooldown();
    }

    private void processInit(ServerPlayerEntity player) {
        if(!hasInit) {
            init(player);
            hasInit = true;
        }
    }

    //Meant to overwritten
    protected void init(ServerPlayerEntity player) {}

    private void handleActiveAbility(ServerPlayerEntity player) {
        if (activeAbility != null) {
            if (!activeAbility.hasInit()) {
                initializeAbility();
            }

            if (!activeAbility.isCancelled()) {
                if (activeAbility.isHoldable()) {
                    stamina -= activeAbility.getStaminaDrain();
                }
                activeAbility.execute(player, this);
            } else {
                activeAbility.refresh();
            }

            if (!activeAbility.isActive() || activeAbility.isCancelled()) {
                activeAbility = null;
            }
        }
    }

    private void initializeAbility() {
        if (cooldown > 0 || stamina < activeAbility.getStaminaDrain()) {
            activeAbility.cancel();
        } else {
            //This if is because holdable abilities process stamina differently
            if (activeAbility.isHoldable()) {
                cooldown += activeAbility.getCooldownAdd();
            } else {
                stamina -= activeAbility.getStaminaDrain();
                cooldown += activeAbility.getCooldownAdd();
            }
        }
        activeAbility.initDone();
    }

    private void processPassives(ServerPlayerEntity player) {
        Iterator<PassiveAbility> passiveIterator = passives.iterator();
        while (passiveIterator.hasNext()) {
            PassiveAbility passive = passiveIterator.next();
            if (passive.isDone(player, this)) {
                passiveIterator.remove();
                player.sendMessage(Text.literal("Removed Passive"));
            }
        }
    }

    private void regenerateStamina() {
        if (stamina < 1000) {
            stamina++;
        }
    }

    private void reduceCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    private void deActivate() {

    }

    public AbilityBase getActiveAbility() {
        return activeAbility;
    }

    public void setActiveAbility(AbilityBase activeAbility) {
        this.activeAbility = activeAbility;
    }

    public void addPassive(PassiveAbility passive) {
        if(!this.passives.contains(passive)) {
            this.passives.add(passive);
        }
    }
    //TODO using this method creates a new instance in memory which then allows the passive to be added twice.
    //TODO fix it
    @Deprecated
    public void addPassive(PassiveAbility passive, int staminaDrain) {
        if(!this.passives.contains(passive)) {
            this.passives.add((player, quirk) -> {
                if(this.getStamina() < staminaDrain) {
                    return true;
                }
                stamina -= staminaDrain;
                passive.isDone(player, quirk);
                return false;
            });
        }
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
}
