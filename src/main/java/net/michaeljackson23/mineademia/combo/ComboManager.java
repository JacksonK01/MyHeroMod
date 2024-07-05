package net.michaeljackson23.mineademia.combo;

import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ComboManager {
    private int amountOfHits = 0;
    private int timer = 0;
    private boolean isInCombo = false;
    private LivingEntity target;
    private ComboType previousComboType = ComboType.NONE;
    private boolean freshHit = false;

    private final int MAX_AMOUNT_OF_HITS = 3;
    private final float DAMAGE = 5f;

    public void tick(ServerPlayerEntity player) {
        if(isInCombo && target != null) {
            target.setAttacker(player);
            findWhichComboType(player);
            if(amountOfHits >= MAX_AMOUNT_OF_HITS) {
                resetCombo();
            }
            timer--;
            if(timer <= 0) {
                resetCombo();
            }
//            player.sendMessage(Text.literal(this.toString()));
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
            increment(target);
        }
    }

    private float findDamage() {
        return amountOfHits * DAMAGE;
    }

    private void resetCombo() {
        timer = 0;
        amountOfHits = 0;
        isInCombo = false;
        target = null;
        freshHit = false;
        previousComboType = ComboType.NONE;
    }

    private void increment(LivingEntity target) {
        this.target = target;
        amountOfHits++;
        timer = 20;
        isInCombo = true;
        freshHit = true;
    }

    private boolean canIncrementCombo(ServerPlayerEntity attacker, LivingEntity target) {
        return attacker.getMainHandStack().isEmpty() && target.hurtTime <= 0 && target.damage(attacker.getDamageSources().playerAttack(attacker), findDamage());
    }

    private void findWhichComboType(ServerPlayerEntity player) {
        if(freshHit) {
            switch (previousComboType) {
                case PUNCH -> doPunchCombo(player);
                case KICK -> doKickCombo(player);
                case AERIAL -> {
                    if(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir()) {
                        doAerialInAirCombo(player);
                    } else {
                        doAerialOnGroundCombo(player);
                    }
                }
            }
            freshHit = false;
        }
    }

    private void doPunchCombo(ServerPlayerEntity player) {
        Vec3d playerVec = getVec3d(player);
        switch (amountOfHits) {
            case 1 -> {
                target.setVelocity(playerVec.multiply(0.5));
                player.setVelocity(target.getVelocity().multiply(0.9));
                AnimationProxy.sendAnimationToClients(player, "combo_punch_1");
                target.getWorld().playSound(null, target.getBlockPos(), CustomSounds.PHYSICAL_DAMAGE_EVENT, SoundCategory.PLAYERS, 1f, 1.2f);
            }
            case 2 -> {
                target.setVelocity(playerVec);
                player.setVelocity(target.getVelocity().multiply(0.9));
                AnimationProxy.sendAnimationToClients(player, "combo_punch_2");
                target.getWorld().playSound(null, target.getBlockPos(), CustomSounds.PHYSICAL_DAMAGE_EVENT, SoundCategory.PLAYERS, 1f, 0.75f);
            }
            case 3 -> {
                target.setVelocity(playerVec.multiply(3));
                player.setVelocity(playerVec);
                AnimationProxy.sendAnimationToClients(player, "combo_punch_3");
                target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 2f);
            }
        }
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY() + 1, target.getZ(),
                1, 0.1, 0.1, 0.1, 1);
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private void doKickCombo(ServerPlayerEntity player) {
        Vec3d playerVec = getVec3d(player);
        switch (amountOfHits) {
            case 1 -> {
                Vec3d kickVel = new Vec3d(playerVec.x, 0.75, playerVec.z);
                target.setVelocity(kickVel.multiply(0.05, 1, 0.05));
                player.setVelocity(target.getVelocity().multiply(0.9));
                AnimationProxy.sendAnimationToClients(player, "combo_kick_1");
                target.getWorld().playSound(null, target.getBlockPos(), CustomSounds.LEG_MOVEMENT_EVENT, SoundCategory.PLAYERS, 1f, 1.2f);
            }
            case 2 -> {
                Vec3d kickVel = new Vec3d(playerVec.x, 0.75, playerVec.z);
                target.setVelocity(kickVel.multiply(0.2, 1, 0.2));
                player.setVelocity(-playerVec.x, 0.75, -playerVec.z);
                AnimationProxy.sendAnimationToClients(player, "combo_kick_2");
                target.getWorld().playSound(null, target.getBlockPos(), CustomSounds.LEG_MOVEMENT_EVENT, SoundCategory.PLAYERS, 1f, 2f);
            }
            case 3 -> {
                target.setVelocity(playerVec.multiply(1, 2, 1));
                player.setVelocity(target.getVelocity());
            }
        }
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY() + 0.1, target.getZ(),
                1, 0.1, 0.1, 0.1, 0);
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private void doAerialOnGroundCombo(ServerPlayerEntity player) {
        switch (amountOfHits) {
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
        }
    }

    private void doAerialInAirCombo(ServerPlayerEntity player) {
        switch (amountOfHits) {
            case 1 -> {

            }
            case 2 -> {

            }
            case 3 -> {

            }
        }
    }

    private Vec3d getVec3d(ServerPlayerEntity player) {
        double yawRad = Math.toRadians(player.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize();
    }

    @Override
    public String toString() {
        return "[Amount of hits: " + amountOfHits + "] [Cooldown " + timer + "] [Type " + previousComboType.getType() + "] [Target: " + (target != null ? target.toString() : "null") + "]";
    }

}
