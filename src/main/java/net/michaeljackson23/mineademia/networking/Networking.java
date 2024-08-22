package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityUserPacketS2C;
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
    public static final Identifier S2C_QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "quirk_tablet_gui");
    public static final Identifier S2C_MOCK_QUIRK_TABLET_GUI = new Identifier(Mineademia.MOD_ID, "mock_quirk_tablet_gui");
    public static final Identifier S2C_ANIMATION = new Identifier(Mineademia.MOD_ID, "animation_proxy");
    public static final Identifier S2C_ANIMATION_NO_API = new Identifier(Mineademia.MOD_ID, "animation_proxy_no_api");
    public static final Identifier S2C_SET_YAW = new Identifier(Mineademia.MOD_ID, "set_yaw");
    public static final Identifier S2C_FORCE_INTO_THIRD_PERSON_BACK = new Identifier(Mineademia.MOD_ID, "force_third_person_back");
    public static final Identifier S2C_FORCE_INTO_THIRD_PERSON_FRONT = new Identifier(Mineademia.MOD_ID, "force_third_person_front");
    public static final Identifier S2C_FORCE_INTO_FIRST_PERSON = new Identifier(Mineademia.MOD_ID, "force_first_person");
    public static final Identifier S2C_COMBO_DAMAGE = new Identifier(Mineademia.MOD_ID, "combo_damage");
    public static final Identifier S2C_OPEN_VESTIGE_GUI = new Identifier(Mineademia.MOD_ID, "vestige_gui");
    public static final Identifier S2C_GLOW_ENTITIES = new Identifier(Mineademia.MOD_ID, "glow_entities");
    public static final Identifier S2C_WIND_FLY_DESCENT_VELOCITY = new Identifier(Mineademia.MOD_ID, "wind_fly_descent_velocity");
    public static final Identifier S2C_DRAW_BOX = new Identifier(Mineademia.MOD_ID, "draw_box");

    public static final Identifier S2C_ZOOM = new Identifier(Mineademia.MOD_ID, "zoom");


    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_ONE, PacketsC2S::abilityOne);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_TWO, PacketsC2S::abilityTwo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_THREE, PacketsC2S::abilityThree);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_FOUR, PacketsC2S::abilityFour);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_FIVE, PacketsC2S::abilityFive);
        ServerPlayNetworking.registerGlobalReceiver(C2S_BLOCKING, PacketsC2S::blocking);
        ServerPlayNetworking.registerGlobalReceiver(C2S_KICK_COMBO, PacketsC2S::kickCombo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_AERIAL_COMBO, PacketsC2S::aerialCombo);
        ServerPlayNetworking.registerGlobalReceiver(C2S_OPEN_QUIRK_GUI, PacketsC2S::openQuirkTabletGUI);
        ServerPlayNetworking.registerGlobalReceiver(C2S_MOCK_CHANGE_QUIRK_WITH_TABLET, PacketsC2S::mockQuirkTabletQuirkChange);
        ServerPlayNetworking.registerGlobalReceiver(C2S_SELECT_VESTIGE_GUI, PacketsC2S::vestigeAbility);
        ServerPlayNetworking.registerGlobalReceiver(C2S_ABILITY_DODGE, PacketsC2S::abilityDodge);


        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TEST, PacketsC2S::abilityTest); // TODO REMOVE!!!
        ServerPlayNetworking.registerGlobalReceiver(ABILITY_TEST_SWAP, PacketsC2S::abilityTestSwap); // TODO REMOVE!!!
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC, PacketsS2C::quirkDataSync);
        ClientPlayNetworking.registerGlobalReceiver(QuirkDataPacket.QUIRK_DATA_SYNC_PROXY, PacketsS2C::quirkDataSyncProxy);
        ClientPlayNetworking.registerGlobalReceiver(S2C_QUIRK_TABLET_GUI, PacketsS2C::quirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(S2C_MOCK_QUIRK_TABLET_GUI, PacketsS2C::mockQuirkTablet);
        ClientPlayNetworking.registerGlobalReceiver(S2C_ANIMATION, PacketsS2C::animationProxy);
        ClientPlayNetworking.registerGlobalReceiver(S2C_ANIMATION_NO_API, PacketsS2C::animationProxyNoAPI);
        ClientPlayNetworking.registerGlobalReceiver(S2C_SET_YAW, PacketsS2C::setYaw);
        ClientPlayNetworking.registerGlobalReceiver(S2C_FORCE_INTO_THIRD_PERSON_BACK, PacketsS2C::forceThirdPersonBack);
        ClientPlayNetworking.registerGlobalReceiver(S2C_FORCE_INTO_THIRD_PERSON_FRONT, PacketsS2C::forceThirdPersonFront);
        ClientPlayNetworking.registerGlobalReceiver(S2C_FORCE_INTO_FIRST_PERSON, PacketsS2C::forceFirstPerson);
        ClientPlayNetworking.registerGlobalReceiver(S2C_OPEN_VESTIGE_GUI, PacketsS2C::openVestigeGui);
        ClientPlayNetworking.registerGlobalReceiver(S2C_GLOW_ENTITIES, PacketsS2C::setEntitiesGlow);
        ClientPlayNetworking.registerGlobalReceiver(S2C_WIND_FLY_DESCENT_VELOCITY, PacketsS2C::windFlyDescentVelocity);
        ClientPlayNetworking.registerGlobalReceiver(S2C_COMBO_DAMAGE, PacketsS2C::comboDamage);

        ClientPlayNetworking.registerGlobalReceiver(AbilityUserPacketS2C.S2C_ABILITY_USER_PACKET, PacketsS2C::playerAbilityUser);
        ClientPlayNetworking.registerGlobalReceiver(S2C_ZOOM, PacketsS2C::zoomPacket);
    }
}
