package net.michaeljackson23.mineademia.combo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ComboManager {
    private int amountOfHits = 0;
    private int cooldown = 0;
    private boolean isInCombo = false;
    private LivingEntity target;
    private ComboType previousComboType;

    public void tick(ServerPlayerEntity player) {
        if(isInCombo && target != null) {
            cooldown--;
            if(cooldown <= 0) {
                resetCombo();
            }

            player.sendMessage(Text.literal(toString()));
        }
    }

    public void notifyPunch(LivingEntity target) {
        this.target = target;
        previousComboType = ComboType.PUNCH;
        if(target.canTakeDamage()) {
            amountOfHits++;
        }
        cooldown = 60;
        isInCombo = true;
    }

    public void notifyKick(LivingEntity target) {
        this.target = target;
        previousComboType = ComboType.KICK;
        if(target.canTakeDamage()) {
            amountOfHits++;
        }
        cooldown = 60;
        isInCombo = true;
    }

    public void notifyAerial(LivingEntity target) {
        this.target = target;
        previousComboType = ComboType.AERIAL;
        if(target.canTakeDamage()) {
            amountOfHits++;
        }
        cooldown = 60;
        isInCombo = true;
    }

    private void resetCombo() {
        cooldown = 0;
        amountOfHits = 0;
        isInCombo = false;
    }

    @Override
    public String toString() {
        return "[Amount of hits: " + amountOfHits + "] [Cooldown " + cooldown + "] [Type " + previousComboType.getType() + "] [Target: " + (target != null ? target.toString() : "null") + "]";
    }

    private enum ComboType {
        PUNCH("punch"),
        KICK("kick"),
        AERIAL("aerial");

        private final String name;

        ComboType(String name) {
            this.name = name;
        }

        public String getType() {
            return name;
        }
    }
}
