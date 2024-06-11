package net.michaeljackson23.mineademia.quirk;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.quirk.quirks.Explosion;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

/**
 * <p>
 *     Everything else is just technical stuff. This is where the fun begins. This is where
 *     the quirk stores everything, and logically processes everything as well. Every single
 *     quirk that is created must extend this object here, and be added to the IndexMap in
 *     {@link QuirkInitialize}.
 * </p>
 * <p>
 *     After reading this, check out {@link net.michaeljackson23.mineademia.quirk.abilities.AbilityBase}
 * </p>
 */
public abstract class Quirk {

    /**
     * <p>
     *     Name of the quirk (Ex. Hellflame or Explosion)
     * </p>
     */
    private final String name;

    /**
     * <p>
     *     This holds all the abilities. The current max is 5 abilities per quirk.
     *     The index of the ability corresponds to the keybind. The Ability One keybind corresponds
     *     with abilities[0] and Ability Two keybind with abilities[1].
     *     See {@link net.michaeljackson23.mineademia.keybinds.Keybinds} for more on keybinds.
     * </p>
     */
    private AbilityBase[] abilities = new AbilityBase[5];

    /**
     * <p>
     *     This variable is purely used a pointer for whichever ability is active for this quirk.
     *     See {@link net.michaeljackson23.mineademia.networking.ServerPackets#activateAbility(ServerPlayerEntity, PacketByteBuf, int)}
     *     for how activeAbility gets set.
     * </p>
     */
    private AbilityBase activeAbility;

    /**
     * <p>
     *     Every ability requires the cooldown to = 0 to activate
     * </p>
     */
    private int cooldown = 0;

    /**
     * <p>
     *     Each ability consumes a specific amount of stamina. To use an ability, the player's stamina must be at least the required amount for that ability.
     * </p>
     */
    private double stamina = 1000;

    //Will detail later
    private ArrayList<Integer> quirkStats = new ArrayList<>();

    /**
     * <p>
     *     Some abilities will require a passive to be activated. Such as the {@link net.michaeljackson23.mineademia.quirk.quirks.OneForAll}
     *     quirk which has the ability {@link net.michaeljackson23.mineademia.quirk.abilities.ofa.Cowling}. The ability cowling
     *     has a variable {@link net.michaeljackson23.mineademia.quirk.abilities.ofa.Cowling#cowling} that is added to the passives.
     * </p>
     */
    private LinkedList<PassiveAbility> passives = new LinkedList<>();

    //Will detail later
    private ArrayList<String> modelsForQuirk = new ArrayList<>();

    //This is not important
    public int tickCounter = 10;

    /**
     * <p>
     *     When creating a new quirk, you don't create a constructor that matches this one.
     *     You fill in the parameters depending on the quirk you want to create. For example see
     *     {@link Explosion#Explosion()}
     * </p>
     *
     * <p>
     *     Used by = {@link net.michaeljackson23.mineademia.quirk.quirks.Electrification},
     *     {@link net.michaeljackson23.mineademia.quirk.quirks.Explosion},
     *     {@link net.michaeljackson23.mineademia.quirk.quirks.HalfColdHalfHot},
     *     {@link net.michaeljackson23.mineademia.quirk.quirks.OneForAll},
     *     {@link net.michaeljackson23.mineademia.quirk.quirks.Quirkless},
     *     {@link net.michaeljackson23.mineademia.quirk.quirks.Whirlwind},
     * </p>
     */
    public Quirk(String name, AbilityBase one, AbilityBase two, AbilityBase three, AbilityBase four, AbilityBase five) {
        this.name = name;
        this.abilities[0] = one;
        this.abilities[1] = two;
        this.abilities[2] = three;
        this.abilities[3] = four;
        this.abilities[4] = five;
    }

    /**
     * <p>
     *     Similarly to how a server has to tick, a player's quirk has tick too.
     *     The code for this method would be too long to all neatly fit in one spot, so
     *     it's split into 4 chunks. This method is called every single tick by the server.
     * </p>
     * <p>
     *     Used by = {@link ServerQuirkTicks#serverTickRegister()}
     * </p>
     */
    public void tick(ServerPlayerEntity player) {
        handleActiveAbility(player);
        processPassives(player);
        regenerateStamina();
        reduceCooldown();
    }

    /**
     * <p>
     *     The logic of this part is hard to explain and just not super important
     *     to understand. Just know this is how the quirk determines if activeAbility is
     *     valid to use and run.
     * </p>
     */
    //TODO fix holdable abilities going into negative stamina
    private void handleActiveAbility(ServerPlayerEntity player) {
        if (activeAbility != null) {
            if (!activeAbility.hasInit()) {
                initializeAbility();
                QuirkDataPacket.send(player);
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

    /**
     * <p>
     *     Helper method for handleActiveAbility
     * </p>
     */
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

//    USE FOR DEBUGGING
//    private void processPassives(ServerPlayerEntity player) {
//        Iterator<PassiveAbility> passiveIterator = passives.iterator();
//        while (passiveIterator.hasNext()) {
//            PassiveAbility passive = passiveIterator.next();
//            if (passive.isDone(player, this)) {
//                passiveIterator.remove();
//                //player.sendMessage(Text.literal("Removed Passive"));
//            }
//        }
//    }

    /**
     * <p>
     *     A passive will return true once it's finished processing.
     *     This is confusing in theory, but seeing a passive being implemented will help understand it better.
     *     As mentioned before, see {@link net.michaeljackson23.mineademia.quirk.abilities.ofa.Cowling#cowling}.
     * </p>
     */
    private void processPassives(ServerPlayerEntity player) {
        passives.removeIf(passive -> passive.isDone(player, this));
    }

    /**
     * <p>
     *     Max for stamina is 1000
     * </p>
     */
    private void regenerateStamina() {
        if (stamina < 1000) {
            stamina++;
        } else {
            stamina = 1000;
        }
    }

    /**
     * <p>
     *     Cooldown doesn't have a max. It's min is 0
     * </p>
     */
    private void reduceCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    /**
     * <p>
     *     Getter method, not important
     * </p>
     */
    public AbilityBase getActiveAbility() {
        return activeAbility;
    }

    /**
     * <p>
     *     Setter method, not important
     * </p>
     */
    public void setActiveAbility(AbilityBase activeAbility) {
        this.activeAbility = activeAbility;
    }

    /**
     * <p>
     *     Will check to see if passive is already in the
     *     passives list before adding it. It's super important
     *     that passives are created in the attribute of an ability, otherwise
     *     they won't have the same memory address.
     * </p>
     * <p>
     *     Used by = A lot of abilities use this, a good example is {@link net.michaeljackson23.mineademia.quirk.abilities.explosion.ExplosionDash#particlesPassive}
     *     which is used to create a neat particle effect.
     * </p>
     */
    public void addPassive(PassiveAbility passive) {
        if(!this.passives.contains(passive)) {
            this.passives.add(passive);
        }
    }

    /**
     * <p>
     *     Everything after this is not important.
     * </p>
     */

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

    public void addModel(String model) {
        if(!modelsForQuirk.contains(model)) {
            this.modelsForQuirk.add(model);
        }
    }

    public void setAbilities(AbilityBase[] abilities) {
        this.abilities = abilities;
    }

    public void setModelsForQuirk(String... models) {
        modelsForQuirk.addAll(Arrays.asList(models));
    }

    public String[] getModelsForQuirk() {
        String[] a = new String[0];
        return this.modelsForQuirk.isEmpty() ? a : modelsForQuirk.toArray(a);
    }

    public void removeModel(String model) {
        modelsForQuirk.remove(model);
    }

    @Override
    public String toString() {
        return this.name +": " + Arrays.toString(this.abilities);
    }
}
