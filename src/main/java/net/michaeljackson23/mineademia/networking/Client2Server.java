package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.QuirkInitialize;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Client2Server {
    //This is for when a server needs to listen for a packet from the client
    public static final Identifier ABILITY_ONE = new Identifier(Mineademia.Mod_id, "ability_one");
    public static final Identifier ABILITY_TWO = new Identifier(Mineademia.Mod_id, "ability_two");
    public static final Identifier ABILITY_THREE = new Identifier(Mineademia.Mod_id, "ability_three");
    public static final Identifier ABILITY_FOUR = new Identifier(Mineademia.Mod_id, "ability_four");
    public static final Identifier ABILITY_FIVE = new Identifier(Mineademia.Mod_id, "ability_five");

    public static final Identifier DODGE = new Identifier(Mineademia.Mod_id, "dodge");

    public static final Identifier QUIRKTABLETGUIOPEN = new Identifier(Mineademia.Mod_id, "quirk_tablet_gui");


    public static void registerClientToServerPackets() {
        Mineademia.LOGGER.info("Registering all Client to Server event listeners");

        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FIVE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> Client2Server.activateAbility(player, buf, 4));
        });

        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FOUR, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> Client2Server.activateAbility(player, buf, 3));
        });

        ServerPlayNetworking.registerGlobalReceiver(ABILITY_THREE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> Client2Server.activateAbility(player, buf, 2));
        });

        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TWO, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> Client2Server.activateAbility(player, buf, 1));
        });

        ServerPlayNetworking.registerGlobalReceiver(ABILITY_ONE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> Client2Server.activateAbility(player, buf, 0));
        });

        ServerPlayNetworking.registerGlobalReceiver(QUIRKTABLETGUIOPEN, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                String quirk = buf.readString();
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                player.sendMessage(Text.literal("Changed quirk to "+quirk));
                QuirkInitialize.setQuirk(playerState, quirk);
            });
        });
    }

    //This where the game checks for cooldown and stamina
    private static void activateAbility(ServerPlayerEntity player, PacketByteBuf buf, int i) {
        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
        boolean isHeld = buf.readBoolean();
        if (isHeld && playerState.getCooldown() == 0) {
            playerState.setActiveAbility(playerState.getAbilities()[i]);
        }
        if(playerState.getActiveAbility() != null) {
            playerState.getActiveAbility().setIsCurrentlyHeld(isHeld);
        }
    }
}
