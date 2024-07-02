package net.michaeljackson23.mineademia.networking;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.gui.vestige.VestigeGUI;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientPackets {
    public static void quirkDataSync(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(!(client.player instanceof QuirkDataAccessors quirkPlayer)) {
            return;
        }

        quirkPlayer.myHeroMod$setQuirkData(QuirkDataPacket.decode(buf));
    }
    //To sync player's QuirkData with every player
    public static void quirkDataSyncProxy(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(client.world != null) {
            QuirkData quirkData = QuirkDataPacket.decode(buf);
            PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
            if(player instanceof QuirkDataAccessors quirkPlayer) {
                quirkPlayer.myHeroMod$setQuirkData(quirkData);
            }
        }
    }
    public static void quirkTablet(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(() -> {
            QuirkTabletGui quirkTabletGui = new QuirkTabletGui(Text.literal("Quirk Tablet"));
            quirkTabletGui.init(client, 50, 50);
            quirkTabletGui.shouldPause();
            client.setScreenAndRender(quirkTabletGui);
        });
    }
    public static void animationProxy(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(() -> {
            UUID playerToAnimate = buf.readUuid();
            String animationName = buf.readString();
            Identifier id = new Identifier(Mineademia.MOD_ID, animationName);
            AbstractClientPlayerEntity player = null;
            if (client.world != null) {
                player = (AbstractClientPlayerEntity) client.world.getPlayerByUuid(playerToAnimate);
            }
            if (player != null) {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                for(int i = 0; animationStack.isActive(); i++) {
                    animationStack.removeLayer(i);
                }
                if(!animationName.equals("reset")) {
                    KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(id);
                    if(keyframeAnimation != null) {
                        animationStack.addAnimLayer(0, new KeyframeAnimationPlayer(keyframeAnimation)
                                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(true)
                                .setShowLeftArm(true)
                                .setShowRightItem(false)
                                .setShowLeftItem(false)));
                    }
                }
            }
        });
    }
    public static void setYaw(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(client.player != null) {
            client.player.setYaw(buf.readFloat());
        }
    }
    public static void forceThirdPersonBack(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }
    public static void forceThirdPersonFront(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
    }
    public static void forceFirstPerson(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.options.setPerspective(Perspective.FIRST_PERSON);
    }

    public static void openVestigeGui(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        client.execute(() -> {
            VestigeGUI vestigeGUI = new VestigeGUI(Text.literal("Quirk Tablet"));
            vestigeGUI.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
            vestigeGUI.shouldPause();
            client.setScreenAndRender(vestigeGUI);
        });
    }
    public static void makeAnEntityGlow(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ClientPlayerEntity player = client.player;
        if(player != null) {

        }
    }
}
