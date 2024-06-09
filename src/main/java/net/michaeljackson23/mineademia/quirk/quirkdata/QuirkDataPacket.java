package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class QuirkDataPacket {
    public static final Identifier QUIRK_DATA_SYNC = new Identifier(Mineademia.MOD_ID, "quirk_data_sync");
    public static final Identifier QUIRK_DATA_SYNC_PROXY = new Identifier(Mineademia.MOD_ID, "quirk_data_sync_all");

    public static PacketByteBuf encode(Quirk quirk) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeString(quirk.getName());
        data.writeInt(quirk.getModelsForQuirk().length);
        for(int i = 0; i < quirk.getModelsForQuirk().length; i++) {
            data.writeString(quirk.getModelsForQuirk()[i]);
        }
        data.writeDouble(quirk.getStamina());
        data.writeInt(quirk.getCooldown());
        return data;
    }

    public static QuirkData decode(PacketByteBuf data) {
        String quirk = data.readString();
        int modelCount = data.readInt();
        String[] models = new String[modelCount];
        for(int i = 0; i < modelCount; i++) {
            models[i] = data.readString();
        }
        double stamina = data.readDouble();
        int cooldown = data.readInt();
        return new QuirkData(quirk, models, stamina, cooldown);
    }

    public static void send(ServerPlayerEntity player) {
        Quirk quirk = StateSaverAndLoader.getPlayerState(player).getQuirk();
        PacketByteBuf data = encode(quirk);
        ServerPlayNetworking.send(player, QUIRK_DATA_SYNC, data);
    }

    public static void sendProxy(ServerPlayerEntity player) {
        Quirk quirk = StateSaverAndLoader.getPlayerState(player).getQuirk();
        //player.sendMessage(Text.literal("[" + player.getName().getString() + "] " + quirkPlayer.myHeroMod$getQuirkData()));
        PacketByteBuf data = encode(quirk);
        data.writeUuid(player.getUuid());
        for (ServerPlayerEntity otherPlayer : Objects.requireNonNull(player.getServer()).getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(otherPlayer, QUIRK_DATA_SYNC_PROXY, data);
        }
    }
}
