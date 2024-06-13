package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.savedata.StateSaverAndLoader;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ServerPackets {

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
        player.sendMessage(Text.literal("Changed quirk to " + quirk));
        ((QuirkAccessor) player).myHeroMod$setQuirk(QuirkInitialize.setQuirkWithString(quirk));
    }

    private static void activateAbility(ServerPlayerEntity player, PacketByteBuf buf, int i) {
        Quirk quirk = StateSaverAndLoader.getPlayerState(player).getQuirk();
        boolean isHeld = buf.readBoolean();
        if (isHeld) {
            if(quirk.getActiveAbility() == null) {
                quirk.setActiveAbility(quirk.getAbilities()[i]);
            }
            if(quirk.getActiveAbility() == quirk.getAbilities()[i]) {
                AbilityBase activeAbility = quirk.getActiveAbility();
                activeAbility.setAmountOfTimesActivated(activeAbility.getAmountOfTimesActivated() + 1);
            }
        }
        if(quirk.getActiveAbility() != null) {
            AbilityBase activeAbility = quirk.getActiveAbility();
            activeAbility.setIsCurrentlyHeld(isHeld);
        }
    }
}
