package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.engine.SlideAndKicks;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.AirForce;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * <p>
 *     The mother class for every ability. To see all the abilities go to the package {@link net.michaeljackson23.mineademia.quirk.abilities}
 * </p>
 * <p>
 *     After reading this, try adding your own custom quirk
 * </p>
 */
//TODO abstract into holdable abilities, infinite, and base abilities
public abstract class AbilityBase {
    protected int timer = 0;
    protected int abilityDuration;
    protected int staminaDrain;
    protected int cooldownAdd;
    protected String title;
    protected String description;
    boolean isInfinite = false;
    /**
     * <p>
     *     If you set isHoldable to true, the Quirk processing the ability created will know
     *     when the player is no longer holding that keybind anymore and automatically call the
     *     {@link #deActivate(ServerPlayerEntity, Quirk)} method.
     * </p>
     */
    private final boolean isHoldable;

    /**
     * <p>
     *     This is only useful for processing the logic of the ability in {@link Quirk}. Generally
     *     speaking this isCurrentlyHeld can be ignored
     * </p>
     */
    private boolean isCurrentlyHeld;

    //Used for logic in AbilitiesTicks
    private boolean isActive = false;
    private boolean hasInit = false;
    private boolean cancel = false;

    /**
     * <p>
     *     Abilities can track the amount of times a player has tried activating it. This is useful
     *     for special cases like {@link SlideAndKicks}
     * </p>
     */
    private int amountOfTimesActivated = 0;


    /**
     * <p>
     *     When creating a new ability, you don't create a constructor that matches this one.
     *     You fill in the parameters depending on the ability you want to create. For example,
     *     see {@link AirForce#AirForce()}
     * </p>
     */
    protected AbilityBase(int abilityDuration, int staminaDrain, int cooldownAdd, boolean isHoldable, boolean isInfinite, String title, String description) {
        this.abilityDuration = abilityDuration;
        this.staminaDrain = staminaDrain;
        this.cooldownAdd = cooldownAdd;
        this.isHoldable = isHoldable;
        this.title = title;
        this.description = description;
        this.isInfinite = isInfinite;
    }

    /**
     * <p>
     *     These next methods are not important, skip to the {@link #activate(ServerPlayerEntity, Quirk)}
     * </p>
     */

    public int getCooldownAdd() {
        return this.cooldownAdd;
    }

    public int getStaminaDrain() {
        return this.staminaDrain;
    }

    public boolean hasInit() {
        return this.hasInit;
    }

    public void initDone() {
        this.hasInit = true;
        if(!cancel) {
            this.isActive = true;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void refresh() {
        this.hasInit = false;
        this.cancel = false;
        this.amountOfTimesActivated = 0;
    }

    public void cancel() {
        this.cancel = true;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setAmountOfTimesActivated(int amount) {
        this.amountOfTimesActivated = amount;
    }

    public int getAmountOfTimesActivated() {
        return this.amountOfTimesActivated;
    }

    //override for custom logic
    protected abstract boolean executeCondition(Quirk quirk);

    public boolean isActive() {
        return isActive;
    }

    public boolean isHoldable() {
        return isHoldable;
    }

    public boolean isCurrentlyHeld() {
        return isCurrentlyHeld;
    }

    public void setIsCurrentlyHeld(boolean isHeld) {
        this.isCurrentlyHeld = isHeld;
    }

    /**
     * <p>
     *     This method is called by the quirk that owns this ability. It handles the logic and processing for the ability.
     * </p>
     */
    public void execute(ServerPlayerEntity player, Quirk quirk) {
        if(!isInfinite) {
            timer++;
        }
        if(executeCondition(quirk) && !cancel) {
            activate(player, quirk);
        } else {
            deActivate(player, quirk);
        }
    }

    /**
     * <p>
     *     This method will require custom implementation when creating an ability.
     *     This is where the code for any ability will go.
     *
     *     See {@link net.michaeljackson23.mineademia.quirk.abilities.hchh.fire.FlameThrower#activate(ServerPlayerEntity, Quirk)}
     * </p>
     */
    protected abstract void activate(ServerPlayerEntity player, Quirk quirk);

    /**
     * <p>
     *     This method sets the ability back to the state it started at. This method
     *     can be called prematurely to end an ability. For example in {@link SlideAndKicks}
     *     when certain conditions aren't met, it'll turn the ability off. IMPORTANT: Some abilities require this method to be overwritten,
     *     make sure to call super.deactivate() if you do. See {@link net.michaeljackson23.mineademia.quirk.abilities.miscellaneous.Griddy#deActivate(ServerPlayerEntity, Quirk)}
     * </p>
     */
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        this.timer = 0;
        this.amountOfTimesActivated = 0;
        this.isActive = false;
        this.hasInit = false;
        this.cancel = false;
    }

    protected void deActivate() {
        this.timer = 0;
        this.amountOfTimesActivated = 0;
        this.isActive = false;
        this.hasInit = false;
        this.cancel = false;
    }

    @Override
    public String toString() {
        return "[" + title + " - " + description + "]";
    }
}
