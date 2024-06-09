package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.michaeljackson23.mineademia.util.StopSoundProxy;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class Slide extends AbilityBase {
    boolean init = false;
    //Yaw is horizontal
    float yaw = 0;
    Vec3d storedVec;

    public Slide() {
        super(40, 80, 45, false, "Slide", "test");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            this.yaw = player.getYaw();
            this.storedVec = getVec3d(player);
            init = true;
            AnimationProxy.sendAnimationToClients(player, "slide");
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.SLIDE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
        if(player.getVelocity().y > 0) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, 0, velocity.z);
        }
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        sendYawPackage(player);
        player.velocityModified = true;
        AreaOfEffect.execute(player, 3, 0.2, player.getX(), player.getY(), player.getX(), (entity) -> {
            entity.setVelocity(player.getVelocity());
            entity.velocityModified = true;
            QuirkDamage.doPhysicalDamage(player, entity, 1.0f);
        });
    }

    @Override
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        super.deactivate(player, quirk);
        StopSoundProxy.execute(player, CustomSounds.SLIDE_ID, SoundCategory.PLAYERS);
        init = false;
        yaw = 0;
        storedVec = null;
        AnimationProxy.sendStopAnimation(player);
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
    }

    private Vec3d getVec3d(ServerPlayerEntity player) {
        double yawRad = Math.toRadians(player.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize().multiply(0.6);
    }

    private void sendYawPackage(ServerPlayerEntity player) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeFloat(this.yaw);
        ServerPlayNetworking.send(player, Networking.SET_YAW, data);
        player.setVelocity(storedVec.x, player.getVelocity().y, storedVec.z);
    }
}
