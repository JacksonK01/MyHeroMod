package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ManchesterSmash extends BasicAbility {
    private boolean init = false;
    private int leapCounter = 0;
    private int counter = 0;
    private boolean hasLeapAnimationPlayed = false;
    private boolean hasManchesterAnimationPlayed = false;
    private int flipCounter = 0;
    private boolean impact = false;
    private boolean isAir = false;
    private boolean shouldStallInAir = false;
    private boolean hasGoneDown = false;
    private Phases currentPhase = Phases.START;

    public ManchesterSmash() {
        super(90, 75, 90, "Manchester Smash", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        init(player);
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        leapPhase(player);
        flipPhase(player);
        downPhase(player, quirk);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        if(impact) {
            onHitGround(player);
        }
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
        AnimationProxy.sendStopAnimation(player);
        counter = 0;
        leapCounter = 0;
        flipCounter = 0;
        init = false;
        isAir = false;
        hasLeapAnimationPlayed = false;
        shouldStallInAir = false;
        hasManchesterAnimationPlayed = false;
        hasGoneDown = false;
        this.currentPhase = Phases.START;
        impact = false;
        quirk.setCooldown(25);
    }

    private void init(ServerPlayerEntity player) {
        if(!init) {
            currentPhase = Phases.START;
            isAir = player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir();
            if(isAir) {
                currentPhase = Phases.FLIP;
                shouldStallInAir = true;
            } else {
                currentPhase = Phases.LEAP;
            }
            player.velocityModified = true;
            init = true;
        }
    }

    private void leapPhase(ServerPlayerEntity player) {
        if(currentPhase == Phases.LEAP) {
            if(!hasLeapAnimationPlayed) {
                AnimationProxy.sendAnimationToClients(player, "charge_up_leap");
                hasLeapAnimationPlayed = true;
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.OFA_CHARGE_EVENT, SoundCategory.PLAYERS, 2f, 1f);
                player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                        player.getX(), player.getY() + 0.2, player.getZ(),
                        10, 0, 0, 0, 0.5
                );
            }
            player.setVelocity(0, player.getVelocity().y, 0);
            leapCounter++;
            if(leapCounter > 8) {
                currentPhase = Phases.FLIP;
                player.setVelocity(0, 1.5, 0);
            }
            player.velocityModified = true;
        }
    }

    private void flipPhase(ServerPlayerEntity player) {
        if(currentPhase == Phases.FLIP) {
            if(!hasManchesterAnimationPlayed) {
                AnimationProxy.sendAnimationToClients(player, "manchester_smash_shoot_style");
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
                hasManchesterAnimationPlayed = true;
            }
            if(shouldStallInAir) {
                stallInAir(player);
            }
            flipCounter++;
            if(flipCounter >= 13) {
                currentPhase = Phases.DOWN;
            }
        }
    }

    private void downPhase(ServerPlayerEntity player, Quirk quirk) {
        if(currentPhase == Phases.DOWN) {
           if(!hasGoneDown) {
               goDown(player);
               hasGoneDown = true;
               player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 2f);
               player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.OFA_RELEASE_EVENT, SoundCategory.PLAYERS, 2f, 1f);
               player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                       player.getX(), player.getY() + 0.2, player.getZ(),
                       10, 0, 0, 0, 0.5
               );
           }
            if(!(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir())) {
                impact = true;
                deActivate(player, quirk);
            }
            if(player.getVelocity().y >= 0) {
                deActivate(player, quirk);
            }
        }
    }
    //TODO add particles from the blocks under the spot of smash, and add block break noise
    private void onHitGround(ServerPlayerEntity player) {
        player.setVelocity(player.getVelocity().x, 1, player.getVelocity().z);
        player.velocityModified = true;
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 2f, 2f);
        player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                player.getX(), player.getY(), player.getZ(),
                1, 0, 0, 0, 0
        );
        player.getServerWorld().spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                player.getX(), player.getY() + 0.2, player.getZ(),
                25, 1, 0, 1, 0.5
        );
        player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY() + 0.2, player.getZ(),
                25, 1, 0, 1, 1
        );
        AreaOfEffect.execute(player, 4, 2, player.getX(), player.getY(), player.getZ(), (entity) -> {
            entity.setVelocity(0, 1, 0);
            entity.velocityModified = true;
            QuirkDamage.doPhysicalDamage(player, entity, isAir ? 7.5f : 12.5f);
            player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    1, 0, 0, 0, 0
            );
        });
    }

    private void stallInAir(ServerPlayerEntity player) {
        player.setVelocity(player.getVelocity().x, 0.1, player.getVelocity().z);
        player.velocityModified = true;
    }

    private void goDown(ServerPlayerEntity player) {
        Vec3d lookVec = player.getRotationVector().multiply(2, 0, 2).add(0, -2, 0);
        player.setVelocity(lookVec);
        player.velocityModified = true;
    }

    private enum Phases {
        START,
        LEAP,
        FLIP,
        DOWN;
    }
}
