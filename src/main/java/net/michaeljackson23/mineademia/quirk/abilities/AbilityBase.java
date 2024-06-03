package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbilityBase {
    protected int timer = 0;
    protected int abilityDuration;
    protected int staminaDrain;
    protected int cooldownAdd;
    protected String title;
    //id will always just be the title
    protected String id;
    protected String description;
    //For abilities where the keybind is held
    private final boolean isHoldable;
    //When the keybind is held, Client2Server will send a boolean to the ability saying it's being held
    private boolean isCurrentlyHeld;
    //Used for logic in AbilitiesTicks
    private boolean isActive = false;
    private boolean hasInit = false;
    private boolean cancel = false;


    //Declare these variables here, don't add parameters to constructor, it's not needed
    protected AbilityBase(int abilityDuration, int staminaDrain, int cooldownAdd, boolean isHoldable, String title, String description) {
        this.abilityDuration = abilityDuration;
        this.staminaDrain = staminaDrain;
        this.cooldownAdd = cooldownAdd;
        this.isHoldable = isHoldable;
        this.title = title;
        this.id = title;
        this.description = description;
    }

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
    }

    public void cancel() {
        this.cancel = true;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    //override for custom logic
    private boolean executeCondition() {
        if(isHoldable) {
            return isCurrentlyHeld;
        } else {
            return timer <= abilityDuration;
        }
    }

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

    public void execute(ServerPlayerEntity player, Quirk quirk) {
        timer++;
        System.out.println("Ability timer = " + timer);
        if(executeCondition() && !cancel) {
            activate(player, quirk);
        } else {
            deactivate(player, quirk);
        }
    }

    protected abstract void activate(ServerPlayerEntity player, Quirk quirk);

    //Override if needed
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        this.timer = 0;
        this.isActive = false;
        this.hasInit = false;
        this.cancel = false;
    }
}
