package net.michaeljackson23.mineademia.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.networking.Networking;
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
    private static HoldableKeybind keyAbilityOne;
    private static HoldableKeybind keyAbilityTwo;
    private static HoldableKeybind keyAbilityThree;
    private static HoldableKeybind keyAbilityFour;
    private static HoldableKeybind keyAbilityFive;
    private static HoldableKeybind keyDodge;

    private static KeyBinding keyKickCombo;
    private static KeyBinding keyAerialCombo;

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
        keyDodge = (HoldableKeybind) KeyBindingHelper.registerKeyBinding(new HoldableKeybind(
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
    }
    public static void keybindActions() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            client.execute(() -> {

                keyAbilityOne.holdAndReleaseAction(ABILITY_ONE);
                keyAbilityTwo.holdAndReleaseAction(ABILITY_TWO);
                keyAbilityThree.holdAndReleaseAction(ABILITY_THREE);
                keyAbilityFour.holdAndReleaseAction(ABILITY_FOUR);
                keyAbilityFive.holdAndReleaseAction(ABILITY_FIVE);

                keyDodge.holdAndReleaseAction(DODGE);

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
