package net.michaeljackson23.mineademia.client.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.client.gui.quirkmenu.QuirkMenuGui;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

import static net.michaeljackson23.mineademia.networking.Networking.*;
import static net.michaeljackson23.mineademia.networking.Networking.C2S_ABILITY_TWO;

/**
 * For any confusion:
 * <a href="https://fabricmc.net/wiki/tutorial:keybinds">Click Me</a>
 */
public class Keybinds {

    private static HoldableKeybind keyAbilityOne;
    private static HoldableKeybind keyAbilityTwo;
    private static HoldableKeybind keyAbilityThree;
    private static HoldableKeybind keyAbilityFour;
    private static HoldableKeybind keyAbilityFive;

    private static HoldableKeybind keyAbilityRightClick;

    private static KeyBinding blocking;
    private static KeyBinding keyDodge;

    private static KeyBinding keyKickCombo;
    private static KeyBinding keyAerialCombo;
    private static KeyBinding quirkMenu;



    private static HoldableKeybind keyTest; // TODO REMOVE!!!
    private static KeyBinding keyTestSwap; // TODO REMOVE!!!

    public static void keysRegister() {
        keyAbilityOne = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_one",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.mineademia.mineademia"
        ));
        keyAbilityTwo = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_two",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "key.mineademia.mineademia"
        ));
        keyAbilityThree = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_three",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "key.mineademia.mineademia"
        ));
        keyAbilityFour = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_four",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "key.mineademia.mineademia"
        ));
        keyAbilityFive = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_five",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "key.mineademia.mineademia"
        ));

        keyAbilityRightClick = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.ability_right_click",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_RIGHT,
                "key.mineademia.mineademia"
        ));

        keyDodge = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.dodge",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.mineademia.mineademia"
        ));

        keyKickCombo = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.kick",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_4,
                "key.mineademia.mineademia"
        ));

        keyAerialCombo = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.aerial",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_5,
                "key.mineademia.mineademia"
        ));

        quirkMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.quirk_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "key.mineademia.mineademia"
        ));

        blocking = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.blocking",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.mineademia.mineademia"
        ));


        // TODO REMOVE
        keyTest = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
                "key.mineademia.test",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.mineademia.mineademia"
        ));

        // TODO REMOVE
        keyTestSwap = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mineademia.test_swap",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.mineademia.mineademia"
        ));
    }
    public static void keybindActions() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            client.execute(() -> {



                keyAbilityOne.holdAndReleaseAction(C2S_ABILITY_ONE);
                keyAbilityTwo.holdAndReleaseAction(C2S_ABILITY_TWO);
                keyAbilityThree.holdAndReleaseAction(C2S_ABILITY_THREE);
                keyAbilityFour.holdAndReleaseAction(C2S_ABILITY_FOUR);
                keyAbilityFive.holdAndReleaseAction(C2S_ABILITY_FIVE);

                keyAbilityRightClick.holdAndReleaseAction(C2S_ABILITY_RIGHT_CLICK);

                if (keyDodge.wasPressed()) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(C2S_ABILITY_DODGE, buf);
                }

                if(quirkMenu.wasPressed()) {
                    QuirkMenuGui quirkMenuGui = new QuirkMenuGui(Text.literal("Quirk Menu"));
                    quirkMenuGui.init(client, 50, 50);
                    client.setScreenAndRender(quirkMenuGui);
                }

                keyTest.holdAndReleaseAction(ABILITY_TEST); // TODO REMOVE!!!
                // TODO REMOVE!!!
                if (keyTestSwap.wasPressed()) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(ABILITY_TEST_SWAP, buf);
                }

                if(keyKickCombo.wasPressed()) {
                    if(client.crosshairTarget instanceof EntityHitResult hitResult) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(hitResult.getEntity().getId());
                        ClientPlayNetworking.send(C2S_KICK_COMBO, buf);
                    }
                }
                if(keyAerialCombo.wasPressed()) {
                    if(client.crosshairTarget instanceof EntityHitResult hitResult) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(hitResult.getEntity().getId());
                        ClientPlayNetworking.send(C2S_AERIAL_COMBO, buf);
                    }
                }
                if(blocking.wasPressed()) {
                    ClientPlayNetworking.send(C2S_BLOCKING, PacketByteBufs.empty());
                }

//                if (keyKickCombo.wasPressed() || keyAerialCombo.wasPressed()) {
//                    if(client.player != null && client.interactionManager != null && client.crosshairTarget != null) {
//                        if(client.crosshairTarget instanceof EntityHitResult hitResult) {
//                            client.interactionManager.attackEntity(client.player, hitResult.getEntity());
//                        }
//                    }
//                }
            });
        });
    }

}
