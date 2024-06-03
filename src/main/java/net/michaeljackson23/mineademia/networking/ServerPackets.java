package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ServerPackets {

    public static void modelLoader(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ServerPlayNetworking.send(player, Networking.REMOVE_ALL_QUIRK_FEATURE, PacketByteBufs.empty());
        String quirk = buf.readString();
        PacketByteBuf data = PacketByteBufs.create();
        data.writeString(quirk);
        ServerPlayNetworking.send(player, Networking.LOAD_QUIRK_FEATURE, data);
    }

    public static void abilityOne(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        activateAbility(player, buf, 0);
    }

    public static void abilityTwo(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        activateAbility(player, buf, 1);
    }

    public static void abilityThree(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        activateAbility(player, buf, 2);
    }

    public static void abilityFour(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        activateAbility(player, buf, 3);
    }

    public static void abilityFive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        activateAbility(player, buf, 4);
    }

    public static void openQuirkTabletGUI(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String quirk = buf.readString();
        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
        player.sendMessage(Text.literal("Changed quirk to " + quirk));
        QuirkInitialize.setQuirkWithString(playerState, quirk);
    }

    private static void activateAbility(ServerPlayerEntity player, PacketByteBuf buf, int i) {
        Quirk quirk = StateSaverAndLoader.getPlayerState(player).getQuirk();
        boolean isHeld = buf.readBoolean();
        if (isHeld) {
            quirk.setActiveAbility(quirk.getAbilities()[i]);
        }
        if(quirk.getActiveAbility() != null) {
            quirk.getActiveAbility().setIsCurrentlyHeld(isHeld);
        }
    }
}