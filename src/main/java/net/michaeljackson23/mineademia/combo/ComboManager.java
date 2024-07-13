package net.michaeljackson23.mineademia.combo;

import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
            handleCombo(player);
        }
    }

    private void handleCombo(ServerPlayerEntity player) {
        target.setAttacker(player);
        doComboAction(player);
        handleReset();
    }

    private void handleReset() {
        if(amountOfHits >= MAX_AMOUNT_OF_HITS) {
            resetCombo();
        }
        timer--;
        if(timer <= 0) {
            resetCombo();
        }
    }

    private float findDamage(ServerPlayerEntity player) {
        float cooldown = player.getAttackCooldownProgress(0.5f);
        player.resetLastAttackedTicks();
        return amountOfHits * DAMAGE * cooldown;
    }

    private void resetCombo() {
        timer = 0;
        amountOfHits = 0;
        isInCombo = false;
        target = null;
        freshHit = false;
        previousComboType = ComboType.NONE;
    }

    private void incrementHandler(ServerPlayerEntity attacker, LivingEntity target, ComboType type) {
        if(canIncrementCombo(attacker, target)) {
            previousComboType = type;
            this.target = target;
            amountOfHits++;
            timer = 20;
            isInCombo = true;
            freshHit = true;
            doDamage(attacker, target, findDamage(attacker));
        }
    }

    private boolean canIncrementCombo(ServerPlayerEntity attacker, LivingEntity target) {
        return attacker.getMainHandStack().isEmpty() && target.hurtTime <= 0;
    }

    private void doDamage(ServerPlayerEntity attacker, LivingEntity target, float amount) {
        target.damage(attacker.getDamageSources().playerAttack(attacker), amount);
    }

    private void doComboAction(ServerPlayerEntity player) {
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
        switch (amountOfHits) {
            case 1 -> punchStep(player, 0.5, 0.3, "combo_punch_1", CustomSounds.PHYSICAL_DAMAGE_EVENT, 1.2f);
            case 2 -> punchStep(player, 1, 0.8, "combo_punch_2", CustomSounds.PHYSICAL_DAMAGE_EVENT, 0.75f);
            case 3 -> punchStep(player, 3, 2.75, "combo_punch_3", SoundEvents.ENTITY_GENERIC_EXPLODE, 2f);
        }
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private void doKickCombo(ServerPlayerEntity player) {
        switch (amountOfHits) {
            case 1 -> kickStep(player, 0.05, 0.75, 0.9, "combo_kick_1", 1.2f, false);
            case 2 -> kickStep(player, 1, 1, 0.8, "combo_kick_2", 2f, false);
            case 3 -> kickStep(player, 1, 0.75, 1, "combo_kick_3", 2f, true);
        }
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private void punchStep(ServerPlayerEntity player, double targetVelocityMultiplier, double playerVelocityMultiplier, String animation, SoundEvent soundEvent, float pitch) {
        Vec3d playerVec = getVec3d(player);
        target.setVelocity(playerVec.multiply(targetVelocityMultiplier));
        player.setVelocity(playerVec.multiply(playerVelocityMultiplier));
        AnimationProxy.sendAnimationToClients(player, animation);
        target.getWorld().playSound(null, target.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 1f, pitch);
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY() + 1, target.getZ(),
                1, 0.1, 0.1, 0.1, 1);
    }

    private void kickStep(ServerPlayerEntity player, double targetVelocityMultiplier, double yVelocity, double playerVelocityMultiplier, String animation, float pitch, boolean isBackwards) {
        Vec3d playerVec = getVec3d(player);
        Vec3d kickVel = new Vec3d(playerVec.x * targetVelocityMultiplier, yVelocity, playerVec.z * targetVelocityMultiplier);
        target.setVelocity(kickVel);
        if(isBackwards) {
            player.setVelocity(new Vec3d(-kickVel.x, kickVel.y, -kickVel.z));
        } else {
            player.setVelocity(kickVel.multiply(playerVelocityMultiplier));
        }
        AnimationProxy.sendAnimationToClients(player, animation);
        target.getWorld().playSound(null, target.getBlockPos(), CustomSounds.LEG_MOVEMENT_EVENT, SoundCategory.PLAYERS, 1f, pitch);
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY() + 0.1, target.getZ(),
                1, 0.1, 0.1, 0.1, 0);
    }

    private void doAerialOnGroundCombo(ServerPlayerEntity player) {
        Vec3d playerVec = getVec3d(player);
        target.setVelocity(0, 1.5, 0);
        player.setVelocity(target.getVelocity().multiply(0.9));
        AnimationProxy.sendAnimationToClients(player, "combo_aerial_ground_1");
        target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY() + 1, target.getZ(),
                1, 0.1, 0.1, 0.1, 1);
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private void doAerialInAirCombo(ServerPlayerEntity player) {
        Vec3d playerVec = getVec3d(player);
        target.setVelocity(0, -0.6, 0);
        player.setVelocity(0, 0.3, 0);
        AnimationProxy.sendAnimationToClients(player, "combo_aerial_air_1");
        target.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                target.getX(), target.getY()+ 0.5, target.getZ(),
                1, 0.1, 0.1, 0.1, 1);
        player.velocityModified = true;
        target.velocityModified = true;
    }

    private Vec3d getVec3d(ServerPlayerEntity player) {
        double yawRad = Math.toRadians(player.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize();
    }

    public void notifyPunch(ServerPlayerEntity attacker, LivingEntity target) {
        incrementHandler(attacker, target, ComboType.PUNCH);
    }

    public void notifyKick(ServerPlayerEntity attacker, LivingEntity target) {
        incrementHandler(attacker, target, ComboType.KICK);
    }

    public void notifyAerial(ServerPlayerEntity attacker, LivingEntity target) {
        incrementHandler(attacker, target, ComboType.AERIAL);
    }

    @Override
    public String toString() {
        return "[Amount of hits: " + amountOfHits + "] [Cooldown " + timer + "] [Type " + previousComboType.getType() + "] [Target: " + (target != null ? target.toString() : "null") + "]";
    }

}
