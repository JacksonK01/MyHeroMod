package net.michaeljackson23.mineademia.quirk.abilities.miscellaneous;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.StopSoundProxy;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class Griddy extends BasicAbility {
    boolean init = false;
    //Yaw is horizontal
    float yaw = 0;
    Vec3d storedVec;

    public Griddy() {
        super(80, 0, 45, "Griddy", "Witerally hitting da griddy");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            this.yaw = player.getYaw();
            this.storedVec = getVec3d(player).multiply(0.6);
            init = true;
            AnimationProxy.sendAnimationToClients(player, "griddy");
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.GRIDDY, SoundCategory.PLAYERS, 1f, 1f);
        }
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        PacketByteBuf data = PacketByteBufs.create();
        data.writeFloat(this.yaw);
        ServerPlayNetworking.send(player, Networking.SET_YAW, data);

        player.setVelocity(storedVec.x, player.getVelocity().y, storedVec.z);
        player.velocityModified = true;
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        // StopSoundProxy.execute(player, ModSounds.GRIDDY_ID, SoundCategory.PLAYERS);
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
        return new Vec3d(x, 0, z).normalize();
    }
}
