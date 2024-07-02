package net.michaeljackson23.mineademia.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class StopSoundProxy {
    public static void execute(ServerPlayerEntity stopSoundFor, Identifier soundToStop, SoundCategory category) {
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(soundToStop, category);
        AreaOfEffect.execute(stopSoundFor.getWorld(), 32, 32, stopSoundFor.getX(), stopSoundFor.getY(), stopSoundFor.getZ(), (entityToAffect -> {
            if(entityToAffect instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(stopSoundS2CPacket);
            }
        }));
    }

    //Most of the time it's for SoundCategory.PLAYERS
    public static void execute(ServerPlayerEntity stopSoundFor, Identifier soundToStop) {
        execute(stopSoundFor, soundToStop, SoundCategory.PLAYERS);
    }
}
