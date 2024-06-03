package net.michaeljackson23.mineademia.util;

import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class StopSound {
    public static void execute(ServerPlayerEntity stopSoundFor, Identifier soundToStop, SoundCategory category) {
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(soundToStop, category);
        stopSoundFor.networkHandler.sendPacket(stopSoundS2CPacket);
    }

    //Most of the time it's for SoundCategory.PLAYERS
    public static void execute(ServerPlayerEntity stopSoundFor, Identifier soundToStop) {
        StopSoundS2CPacket stopSoundS2CPacket = new StopSoundS2CPacket(soundToStop, SoundCategory.PLAYERS);
        stopSoundFor.networkHandler.sendPacket(stopSoundS2CPacket);
    }
}
