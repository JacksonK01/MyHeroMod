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

public class ShootStyleKicks extends BasicAbility {
    private boolean init = false;
    private boolean isAir = false;
    private boolean hasDoneAOE = false;

    public ShootStyleKicks() {
        super(20, 10, 20, "Shoot Style Kick", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        init(player);
        abilityAction(player, quirk);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        init = false;
        isAir = false;
        hasDoneAOE = false;
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
    }

    private void init(ServerPlayerEntity player) {
        if(!init) {
            isAir = player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir();
            if(isAir) {
                AnimationProxy.sendAnimationToClients(player, "shoot_style_kick_in_air");
            } else {
                AnimationProxy.sendAnimationToClients(player, "shoot_style_kick_on_ground");
            }
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 2f, 1f);
            init = true;
        }
    }

    private void abilityAction(ServerPlayerEntity player, Quirk quirk) {
        if(isAir) {
            inAirAction(player, quirk);
        } else {
            onGroundAction(player);
        }
    }

    private void inAirAction(ServerPlayerEntity player, Quirk quirk) {
        player.setVelocity(player.getVelocity().x, 0, player.getVelocity().z);
        player.velocityModified = true;
        int IN_AIR_TIMER = 10;
        if(!hasDoneAOE && this.timer >= 8) {
            hasDoneAOE = true;
            AreaOfEffect.execute(player, 4, 2, player.getX(), player.getY(), player.getZ(), (entity) -> {
                entity.setVelocity(0, -2, 0);
                entity.velocityModified = true;
                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                        player.getX(), player.getY() + 1, player.getZ(),
                        1, 0, 0, 0, 0
                );
                QuirkDamage.doPhysicalDamage(player, entity, 4f);
            });
            player.getServerWorld().spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1, player.getZ(),
                    10, 0, 0, 0, 1
            );
            legSound(player);
        }
        if(this.timer >= IN_AIR_TIMER) {
            deActivate(player, quirk);
        }
    }

    private void onGroundAction(ServerPlayerEntity player) {
        player.setVelocity(0, 0, 0);
        player.velocityModified = true;
        if(!hasDoneAOE && this.timer >= 10) {
            hasDoneAOE = true;
            AreaOfEffect.execute(player, 4, 2, player.getX(), player.getY(), player.getZ(), (entity) -> {
                entity.setVelocity(0, 1, 0);
                entity.velocityModified = true;
                QuirkDamage.doPhysicalDamage(player, entity, 4f);
            });
            player.getServerWorld().spawnParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1, player.getZ(),
                    10, 0, 0, 0, 1
            );
            legSound(player);
        }
    }

    private void legSound(ServerPlayerEntity player) {
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.LEG_MOVEMENT_EVENT, SoundCategory.PLAYERS, 2f, 1f);
    }
}
