package net.michaeljackson23.mineademia.combo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ComboManager {
    private int amountOfHits = 0;
    private int timer = 0;
    private boolean isInCombo = false;
    private LivingEntity target;
    private ComboType previousComboType;

    public void tick(ServerPlayerEntity player) {
        if(isInCombo && target != null) {
            timer--;
            if(timer <= 0) {
                resetCombo();
            }
            if(amountOfHits >= 4) {
                target.setVelocity(player.getRotationVector().multiply(3));
                target.velocityModified = true;
                resetCombo();
            }
            player.sendMessage(Text.literal(toString()));
        }
    }

    public void notifyPunch(ServerPlayerEntity attacker, LivingEntity target) {
        if(canIncrementCombo(attacker, target)) {
            previousComboType = ComboType.PUNCH;
            increment(target);
        }
    }

    public void notifyKick(ServerPlayerEntity attacker, LivingEntity target) {
        if(canIncrementCombo(attacker, target)) {
            previousComboType = ComboType.KICK;
            increment(target);
        }
    }

    public void notifyAerial(ServerPlayerEntity attacker, LivingEntity target) {
        if(canIncrementCombo(attacker, target)) {
            previousComboType = ComboType.AERIAL;
            target.damage(attacker.getDamageSources().playerAttack(attacker), 1f);
            increment(target);
        }
    }

    private void resetCombo() {
        timer = 0;
        amountOfHits = 0;
        isInCombo = false;
        target = null;
    }

    private void increment(LivingEntity target) {
        this.target = target;
        if(target.canTakeDamage()) {
            amountOfHits++;
        }
        timer = 60;
        isInCombo = true;
    }

    private boolean canIncrementCombo(ServerPlayerEntity attacker, LivingEntity target) {
        return attacker.getMainHandStack().isEmpty() && target.damage(attacker.getDamageSources().playerAttack(attacker), 1f);
    }

    @Override
    public String toString() {
        return "[Amount of hits: " + amountOfHits + "] [Cooldown " + timer + "] [Type " + previousComboType.getType() + "] [Target: " + (target != null ? target.toString() : "null") + "]";
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
