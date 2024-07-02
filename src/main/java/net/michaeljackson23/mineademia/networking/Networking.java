package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.minecraft.util.Identifier;

/**
 * For any confusion:
 * <a href="https://fabricmc.net/wiki/tutorial:networking">Click Me</a>
 */
public class Networking {
    //Server Identifiers sent from client
    public static final Identifier ABILITY_ONE = new Identifier(Mineademia.MOD_ID, "ability_one");
    public static final Identifier ABILITY_TWO = new Identifier(Mineademia.MOD_ID, "ability_two");
    public static final Identifier ABILITY_THREE = new Identifier(Mineademia.MOD_ID, "ability_three");
    public static final Identifier ABILITY_FOUR = new Identifier(Mineademia.MOD_ID, "ability_four");
    public static final Identifier ABILITY_FIVE = new Identifier(Mineademia.MOD_ID, "ability_five");
    public static final Identifier DODGE = new Identifier(Mineademia.MOD_ID, "dodge");
    public static final Identifier OPEN_QUIRK_GUI = new Identifier(Mineademia.MOD_ID, "open_quirk_tablet_gui");
    public static final Identifier SELECT_VESTIGE_GUI = new Identifier(Mineademia.MOD_ID, "vestige_gui");

    //Client Identifiers sent from server
    public static final Identifier QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "quirk_tablet_gui");
    public static final Identifier ANIMATION = new Identifier(Mineademia.MOD_ID, "animation_proxy");
    public static final Identifier SET_YAW = new Identifier(Mineademia.MOD_ID, "set_yaw");
    public static final Identifier FORCE_INTO_THIRD_PERSON_BACK = new Identifier(Mineademia.MOD_ID, "force_third_person_back");
    public static final Identifier FORCE_INTO_THIRD_PERSON_FRONT = new Identifier(Mineademia.MOD_ID, "force_third_person_front");
    public static final Identifier FORCE_INTO_FIRST_PERSON = new Identifier(Mineademia.MOD_ID, "force_first_person");

    public static final Identifier OPEN_VESTIGE_GUI = new Identifier(Mineademia.MOD_ID, "vestige_gui");
    public static final Identifier DANGER_SENSE = new Identifier(Mineademia.MOD_ID, "danger_sense");


    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_ONE, ServerPackets::abilityOne);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TWO, ServerPackets::abilityTwo);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_THREE, ServerPackets::abilityThree);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FOUR, ServerPackets::abilityFour);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FIVE, ServerPackets::abilityFive);
        ServerPlayNetworking.registerGlobalReceiver(OPEN_QUIRK_GUI, ServerPackets::openQuirkTabletGUI);
        ServerPlayNetworking.registerGlobalReceiver(SELECT_VESTIGE_GUI, ServerPackets::vestigeAbility);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC, ClientPackets::quirkDataSync);
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC_PROXY, ClientPackets::quirkDataSyncProxy);
        ClientPlayNetworking.registerGlobalReceiver(QUIRK_TABLET_GUI, ClientPackets::quirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(ANIMATION, ClientPackets::animationProxy);
        ClientPlayNetworking.registerGlobalReceiver(SET_YAW, ClientPackets::setYaw);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_THIRD_PERSON_BACK, ClientPackets::forceThirdPersonBack);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_THIRD_PERSON_FRONT, ClientPackets::forceThirdPersonFront);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_FIRST_PERSON, ClientPackets::forceFirstPerson);
        ClientPlayNetworking.registerGlobalReceiver(OPEN_VESTIGE_GUI, ClientPackets::openVestigeGui);
        ClientPlayNetworking.registerGlobalReceiver(DANGER_SENSE, ClientPackets::makeAnEntityGlow);
    }
}
