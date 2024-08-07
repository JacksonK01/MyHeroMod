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

/**
 * For any confusion:
 * <a href="https://fabricmc.net/wiki/tutorial:keybinds">Click Me</a>
 */
public class Keybinds {
    public static final float DASH_STRENGHT = 10f;
    private static HoldableKeybind keyAbilityOne;
    private static HoldableKeybind keyAbilityTwo;
    private static HoldableKeybind keyAbilityThree;
    private static HoldableKeybind keyAbilityFour;
    private static HoldableKeybind keyAbilityFive;
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

                keyAbilityOne.holdAndReleaseAction(ABILITY_ONE);
                keyAbilityTwo.holdAndReleaseAction(ABILITY_TWO);
                keyAbilityThree.holdAndReleaseAction(ABILITY_THREE);
                keyAbilityFour.holdAndReleaseAction(ABILITY_FOUR);
                keyAbilityFive.holdAndReleaseAction(ABILITY_FIVE);

                if (keyDodge.wasPressed()) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(ABILITY_DODGE, buf);
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
                        ClientPlayNetworking.send(KICK_COMBO, buf);
                    }
                }
                if(keyAerialCombo.wasPressed()) {
                    if(client.crosshairTarget instanceof EntityHitResult hitResult) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(hitResult.getEntity().getId());
                        ClientPlayNetworking.send(AERIAL_COMBO, buf);
                    }
                }
                if(blocking.wasPressed()) {
                    ClientPlayNetworking.send(BLOCKING, PacketByteBufs.empty());
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
