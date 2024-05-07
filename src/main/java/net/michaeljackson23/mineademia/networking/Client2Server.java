package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilities.abilityinit.AbilityMap;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.QuirkInitialize;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
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
        //This is the ability one packet
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FIVE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
//                if(playerState.quirkAbilityTimers[1] == 0) {
//                    playerState.quirkAbilityTimers[1] = 1;
//                }
                playerState.quirkAbilityTimers[4] = 1;

            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TWO, (server, player, handler, buf, responseSender) -> {
                    server.execute(() -> {
                        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
//                if(playerState.quirkAbilityTimers[1] == 0) {
//                    playerState.quirkAbilityTimers[1] = 1;
//                }
                        playerState.quirkAbilityTimers[1] = 1;

                    });
                });
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_ONE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                if (buf.readBoolean() && playerState.quirkCooldown == 0) {
                    playerState.abilityActive[0] = true;
                    AbilityMap abilitiesMap = new AbilityMap(player, playerState, server, 0);
                    playerState.abilityStack.add(abilitiesMap.abilityHandlerMap.get(playerState.quirkAbilities[0]));
                }
                playerState.keyBindsHeld[0] = buf.readBoolean();
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(QUIRKTABLETGUIOPEN, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                String quirk = buf.readString();
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                player.sendMessage(Text.literal("Changed quirk to "+quirk));
                playerState = QuirkInitialize.setQuirk(playerState, quirk);
            });
        });
    }
}
