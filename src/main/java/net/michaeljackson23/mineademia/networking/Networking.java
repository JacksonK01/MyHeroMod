package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.util.Identifier;

public class Networking {
    //Server Identifiers sent from client
    public static final Identifier ABILITY_ONE = new Identifier(Mineademia.MOD_ID, "ability_one");
    public static final Identifier ABILITY_TWO = new Identifier(Mineademia.MOD_ID, "ability_two");
    public static final Identifier ABILITY_THREE = new Identifier(Mineademia.MOD_ID, "ability_three");
    public static final Identifier ABILITY_FOUR = new Identifier(Mineademia.MOD_ID, "ability_four");
    public static final Identifier ABILITY_FIVE = new Identifier(Mineademia.MOD_ID, "ability_five");
    public static final Identifier DODGE = new Identifier(Mineademia.MOD_ID, "dodge");
    public static final Identifier OPEN_QUIRK_GUI = new Identifier(Mineademia.MOD_ID, "open_quirk_tablet_gui");

    //Client Identifiers sent from server
    public static final Identifier QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "quirk_tablet_gui");
    public static final Identifier ANIMATION = new Identifier(Mineademia.MOD_ID, "animation_proxy");
    public static final Identifier QUIRK_DATA = new Identifier(Mineademia.MOD_ID, "quirk_data");
    public static final Identifier LOAD_QUIRK_FEATURE = new Identifier(Mineademia.MOD_ID, "load_quirk_feature");
    public static final Identifier REMOVE_QUIRK_FEATURE = new Identifier(Mineademia.MOD_ID, "remove_quirk_feature");
    public static final Identifier REMOVE_ALL_QUIRK_FEATURE = new Identifier(Mineademia.MOD_ID, "remove_all_quirk_feature");
    public static final Identifier SET_YAW = new Identifier(Mineademia.MOD_ID, "set_yaw");



    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_ONE, ServerPackets::abilityOne);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TWO, ServerPackets::abilityTwo);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_THREE, ServerPackets::abilityThree);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FOUR, ServerPackets::abilityFour);
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_FIVE, ServerPackets::abilityFive);
        ServerPlayNetworking.registerGlobalReceiver(OPEN_QUIRK_GUI, ServerPackets::openQuirkTabletGUI);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(QUIRK_TABLET_GUI, ClientPackets::quirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(ANIMATION, ClientPackets::animationProxy);
        ClientPlayNetworking.registerGlobalReceiver(QUIRK_DATA, ClientPackets::quirkData);
        ClientPlayNetworking.registerGlobalReceiver(SET_YAW, ClientPackets::setYaw);
        ClientPlayNetworking.registerGlobalReceiver(LOAD_QUIRK_FEATURE, ClientPackets::loadQuirkFeature);
        ClientPlayNetworking.registerGlobalReceiver(REMOVE_QUIRK_FEATURE, ClientPackets::removeQuirkFeature);
        ClientPlayNetworking.registerGlobalReceiver(REMOVE_ALL_QUIRK_FEATURE, ClientPackets::removeAllQuirkFeature);

    }
}
