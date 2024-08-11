package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.networking.PlayerAbilityUserPacketS2C;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.minecraft.util.Identifier;

/**
 * For any confusion:
 * <a href="https://fabricmc.net/wiki/tutorial:networking">Click Me</a>
 */
public class Networking {
    //Server Identifiers sent from client
    public static final Identifier C2S_ABILITY_ONE = new Identifier(Mineademia.MOD_ID, "ability_one");
    public static final Identifier C2S_ABILITY_TWO = new Identifier(Mineademia.MOD_ID, "ability_two");
    public static final Identifier C2S_ABILITY_THREE = new Identifier(Mineademia.MOD_ID, "ability_three");
    public static final Identifier C2S_ABILITY_FOUR = new Identifier(Mineademia.MOD_ID, "ability_four");
    public static final Identifier C2S_ABILITY_FIVE = new Identifier(Mineademia.MOD_ID, "ability_five");
    public static final Identifier C2S_ABILITY_DODGE = new Identifier(Mineademia.MOD_ID, "ability_dodge");
    public static final Identifier C2S_BLOCKING = new Identifier(Mineademia.MOD_ID, "blocking");
    public static final Identifier C2S_KICK_COMBO = new Identifier(Mineademia.MOD_ID, "kick_combo");
    public static final Identifier C2S_AERIAL_COMBO = new Identifier(Mineademia.MOD_ID, "aerial_combo");
    public static final Identifier C2S_OPEN_QUIRK_GUI = new Identifier(Mineademia.MOD_ID, "open_quirk_tablet_gui");
    public static final Identifier C2S_SELECT_VESTIGE_GUI = new Identifier(Mineademia.MOD_ID, "vestige_gui");
    public static final Identifier C2S_MOCK_CHANGE_QUIRK_WITH_TABLET = new Identifier(Mineademia.MOD_ID, "mock_change_quirk_with_tablet");


    public static final Identifier ABILITY_TEST = new Identifier(Mineademia.MOD_ID, "ability_test"); // TODO REMOVE!!!
    public static final Identifier ABILITY_TEST_SWAP = new Identifier(Mineademia.MOD_ID, "ability_test_swap"); // TODO REMOVE!!!

    //Client Identifiers sent from server
    public static final Identifier QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "quirk_tablet_gui");
    public static final Identifier MOCK_QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "mock_quirk_tablet_gui");
    public static final Identifier ANIMATION = new Identifier(Mineademia.MOD_ID, "animation_proxy");
    public static final Identifier ANIMATION_NO_API = new Identifier(Mineademia.MOD_ID, "animation_proxy_no_api");
    public static final Identifier SET_YAW = new Identifier(Mineademia.MOD_ID, "set_yaw");
    public static final Identifier FORCE_INTO_THIRD_PERSON_BACK = new Identifier(Mineademia.MOD_ID, "force_third_person_back");
    public static final Identifier FORCE_INTO_THIRD_PERSON_FRONT = new Identifier(Mineademia.MOD_ID, "force_third_person_front");
    public static final Identifier FORCE_INTO_FIRST_PERSON = new Identifier(Mineademia.MOD_ID, "force_first_person");
    public static final Identifier COMBO_DAMAGE = new Identifier(Mineademia.MOD_ID, "combo_damage");
    public static final Identifier OPEN_VESTIGE_GUI = new Identifier(Mineademia.MOD_ID, "vestige_gui");
    public static final Identifier GLOW_ENTITIES = new Identifier(Mineademia.MOD_ID, "glow_entities");
    public static final Identifier WIND_FLY_DESCENT_VELOCITY = new Identifier(Mineademia.MOD_ID, "wind_fly_descent_velocity");
    public static final Identifier DRAW_BOX = new Identifier(Mineademia.MOD_ID, "draw_box");


    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_ONE, ServerPackets::abilityOne);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_TWO, ServerPackets::abilityTwo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_THREE, ServerPackets::abilityThree);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_FOUR, ServerPackets::abilityFour);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_FIVE, ServerPackets::abilityFive);
        ServerPlayNetworking.registerGlobalReceiver(C2S_BLOCKING, ServerPackets::blocking);
        ServerPlayNetworking.registerGlobalReceiver(C2S_KICK_COMBO, ServerPackets::kickCombo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_AERIAL_COMBO, ServerPackets::aerialCombo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_QUIRK_GUI, ServerPackets::openQuirkTabletGUI);
        ServerPlayNetworking.registerGlobalReceiver(C2S_MOCK_CHANGE_QUIRK_WITH_TABLET, ServerPackets::mockQuirkTabletQuirkChange);
        ServerPlayNetworking.registerGlobalReceiver(C2S_SELECT_VESTIGE_GUI, ServerPackets::vestigeAbility);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_DODGE, ServerPackets::abilityDodge);


        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TEST, ServerPackets::abilityTest); // TODO REMOVE!!!
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TEST_SWAP, ServerPackets::abilityTestSwap); // TODO REMOVE!!!
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC, ClientPackets::quirkDataSync);
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC_PROXY, ClientPackets::quirkDataSyncProxy);
        ClientPlayNetworking.registerGlobalReceiver(QUIRK_TABLET_GUI, ClientPackets::quirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(MOCK_QUIRK_TABLET_GUI, ClientPackets::mockQuirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(ANIMATION, ClientPackets::animationProxy);
        ClientPlayNetworking.registerGlobalReceiver(ANIMATION_NO_API, ClientPackets::animationProxyNoAPI);
        ClientPlayNetworking.registerGlobalReceiver(SET_YAW, ClientPackets::setYaw);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_THIRD_PERSON_BACK, ClientPackets::forceThirdPersonBack);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_THIRD_PERSON_FRONT, ClientPackets::forceThirdPersonFront);
        ClientPlayNetworking.registerGlobalReceiver(FORCE_INTO_FIRST_PERSON, ClientPackets::forceFirstPerson);
        ClientPlayNetworking.registerGlobalReceiver(OPEN_VESTIGE_GUI, ClientPackets::openVestigeGui);
        ClientPlayNetworking.registerGlobalReceiver(GLOW_ENTITIES, ClientPackets::setEntitiesGlow);
        ClientPlayNetworking.registerGlobalReceiver(WIND_FLY_DESCENT_VELOCITY, ClientPackets::windFlyDescentVelocity);
        ClientPlayNetworking.registerGlobalReceiver(COMBO_DAMAGE, ClientPackets::comboDamage);

        ClientPlayNetworking.registerGlobalReceiver(PlayerAbilityUserPacketS2C.PLAYER_ABILITY_USER_PACKET, ClientPackets::playerAbilityUser);
    }
}
