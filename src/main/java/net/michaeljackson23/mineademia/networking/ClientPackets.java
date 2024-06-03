package net.michaeljackson23.mineademia.networking;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.hud.DevHudElements;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientPackets {
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
                if(animationStack.isActive()) {
                    animationStack.removeLayer(0);
                }
                KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(id);
                if(keyframeAnimation != null) {
                    animationStack.addAnimLayer(0, new KeyframeAnimationPlayer(keyframeAnimation));
                }
            }
        });
    }
    public static void quirkData(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        DevHudElements.quirk = buf.readString();
        DevHudElements.stamina = buf.readDouble();
        DevHudElements.cooldown = buf.readInt();
    }
    public static void setYaw(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(client.player != null) {
            client.player.setYaw(buf.readFloat());
        }
    }
    public static void loadQuirkFeature(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        QuirkFeatureRenderer.activateModel(buf.readString());
    }
    public static void removeQuirkFeature(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        QuirkFeatureRenderer.deActivateModel(buf.readString());
    }
    public static void removeAllQuirkFeature(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        QuirkFeatureRenderer.deActivateAllModels();
    }
    public static void quirkModelInit(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ClientPlayNetworking.send(Networking.QUIRK_FEATURE_INIT, PacketByteBufs.empty());
    }
}
