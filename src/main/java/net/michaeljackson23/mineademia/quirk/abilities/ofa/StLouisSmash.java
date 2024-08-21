package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;
import java.util.Random;

public class StLouisSmash extends BasicAbility {
    private boolean init = false;
    private int timer = 0;
    private boolean hasHit = false;
    private ArrayList<LivingEntity> entityList = new ArrayList<>();
    private Random random = new Random();

    public StLouisSmash() {
        super(40, 150, 45, "St. Louis Smash", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            if(random.nextBoolean()) {
                AnimationProxy.sendAnimationToClients(player, "st_louis_smash_right_leg");
            } else {
                AnimationProxy.sendAnimationToClients(player, "st_louis_smash_left_leg");
            }
            init = true;
            player.getServerWorld().playSound(null, player.getBlockPos(), ModSounds.OFA_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
        }
        player.setVelocity(0, 0, 0);
        player.velocityModified = true;
        if(timer >= 25 && !hasHit) {
            AreaOfEffect.execute(player, 5.5, 2.5, player.getX(), player.getY(), player.getZ(), (entityToAffect) -> {
                entityToAffect.setVelocity(player.getRotationVector().multiply(3).add(0, 1, 0));
                entityToAffect.velocityModified = true;
                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION, entityToAffect.getX(), entityToAffect.getY() + 1, entityToAffect.getZ(), 1, 0, 0, 0, 0);
                QuirkDamage.doPhysicalDamage(player, entityToAffect, 15f);
                entityList.add(entityToAffect);
            });
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 50, 0, 0, 0, 1);
            player.getServerWorld().playSound(null, player.getBlockPos(), ModSounds.OFA_RELEASE, SoundCategory.PLAYERS, 1f, 1f);
            hasHit = true;
        } else if(!hasHit){
            player.getServerWorld().spawnParticles(ParticleTypes.CLOUD, player.getX(), player.getY(), player.getZ(), 10, -1, -1, -1, 0.01);
            AreaOfEffect.execute(player, 5, 2, player.getX(), player.getY(), player.getZ(), (entityToAffect) -> {
                player.getServerWorld().spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, entityToAffect.getX(), entityToAffect.getY(), entityToAffect.getZ(), 3, 0, 0, 0, 1);
                entityToAffect.setVelocity(0, 0, 0);
                entityToAffect.velocityModified = true;
            });
        }
        timer++;
        ServerPlayNetworking.send(player, Networking.S2C_FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        if(!entityList.isEmpty()) {
            entityList.forEach((entity) -> {
                player.getServerWorld().spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 1, entity.getZ(), 10, 0.3, 0.5, 0.3, 0);
            });
        }
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        hasHit = false;
        timer = 0;
        ServerPlayNetworking.send(player, Networking.S2C_FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
        init = false;
        entityList.clear();
    }
}
