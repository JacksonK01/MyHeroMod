package net.michaeljackson23.mineademia.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;


public class AnimationProxy {
    //Only for serverside activation
    public static void sendAnimationToClients(ServerPlayerEntity playerToAnimate, String animation) {
        MinecraftServer server = playerToAnimate.getServer();
        if(server != null) {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeUuid(playerToAnimate.getUuid());
                data.writeString(animation);
                ServerPlayNetworking.send(player, Networking.S2C_ANIMATION, data);
            });
        }
    }

    public static void sendAnimationToClients(LivingEntity toAnimate, String animation) {
        MinecraftServer server = toAnimate.getServer();
        if(server != null) {
            server.getPlayerManager().getPlayerList().forEach((player) -> {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeInt(toAnimate.getId());
                data.writeString(animation);
                ServerPlayNetworking.send(player, Networking.S2C_ANIMATION_NO_API, data);
            });
        }
    }

    //For cancelling an active animation
    public static void sendStopAnimation(ServerPlayerEntity playerToAnimate) {
        sendAnimationToClients(playerToAnimate, "reset");
    }

    public static void sendStopAnimation(LivingEntity toAnimate) {
        sendAnimationToClients(toAnimate, "reset");
    }
}
