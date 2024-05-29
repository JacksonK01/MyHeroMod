package net.michaeljackson23.mineademia.quirk.abilities;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbilityBase {
    protected int timer = 0;
    protected int abilityDuration;
    protected int staminaDrain;
    protected int cooldownAdd;
    protected String title;
    protected String description;
    //For abilities where the keybind is held
    private final boolean isHoldable;
    //When the keybind is held, Client2Server will send a boolean to the ability saying it's being held
    private boolean isCurrentlyHeld;
    //Used for logic in AbilitiesTicks
    private boolean isActive = false;
    private boolean hasInit = false;


    //Declare these variables here, don't add parameters to constructor, it's not needed
    protected AbilityBase(int abilityDuration, int staminaDrain, int cooldownAdd, boolean isHoldable, String title, String description) {
        this.abilityDuration = abilityDuration;
        this.staminaDrain = staminaDrain;
        this.cooldownAdd = cooldownAdd;
        this.isHoldable = isHoldable;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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
        isActive = true;
        if(!hasInit) {
            if(quirk.getCooldown() > 0 || quirk.getStamina() < staminaDrain) {
                deactivate();
                return;
            }
            applyCooldownAndStamina(quirk);
            hasInit = true;
        }
        timer++;
        if(executeCondition()) {
            activate(player, quirk);
        } else {
            deactivate();
        }
    }

    private void applyCooldownAndStamina(Quirk quirk) {
        quirk.setStamina(quirk.getStamina() - staminaDrain);
        quirk.setCooldown(quirk.getCooldown() + cooldownAdd);
    }

    protected abstract void activate(ServerPlayerEntity player, Quirk quirk);

    //Override if needed
    protected void deactivate() {
        this.timer = 0;
        isActive = false;
        hasInit = false;
    }
}
