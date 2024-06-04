package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class QuirkDataPacket {
    public static final Identifier QUIRK_DATA_SYNC = new Identifier(Mineademia.MOD_ID, "quirk_data_sync");

    public static PacketByteBuf encode(QuirkData quirkData) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeString(quirkData.getQuirkName());
        data.writeInt(quirkData.getModelsArrayLength());
        for(int i = 0; i < quirkData.getModelsArrayLength(); i++) {
            data.writeString(quirkData.getModel(i));
        }
        data.writeDouble(quirkData.getStamina());
        data.writeInt(quirkData.getCooldown());
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
        if(!(player instanceof QuirkDataHelper quirkPlayer)) {
            return;
        }
        quirkPlayer.myHeroMod$getQuirkData().syncStaminaAndCooldown(quirkPlayer.myHeroMod$getQuirk(player.getServer()));
        PacketByteBuf data = encode(quirkPlayer.myHeroMod$getQuirkData());
        ServerPlayNetworking.send(player, QUIRK_DATA_SYNC, data);
    }
}
