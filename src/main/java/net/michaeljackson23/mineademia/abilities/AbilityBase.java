package net.michaeljackson23.mineademia.abilities;

import net.michaeljackson23.mineademia.init.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AbilityBase {
    protected int timer = 0;
    protected int abilityDuration;
    protected int staminaDrain;
    protected int cooldownAdd;
    protected String title;
    protected String description;
    private boolean isActive = false;
    protected boolean isLoop;

    //Declare these variables here, don't add parameters to constructor, it's not needed
    public AbilityBase() {
        this.abilityDuration = 0;
        this.staminaDrain = 0;
        this.cooldownAdd = 0;
        this.isLoop = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void execute(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        isActive = true;
        timer++;
        if(timer <= abilityDuration) {
            activate(player, playerData, server);
        } else {
            deactivate();
        }
    }

    protected abstract void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server);

    //Override if needed
    protected void deactivate() {
        this.timer = 0;
        isActive = false;
    }
}
