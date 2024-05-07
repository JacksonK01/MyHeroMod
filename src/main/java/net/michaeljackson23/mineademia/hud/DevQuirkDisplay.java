package net.michaeljackson23.mineademia.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.init.StateSaverAndLoader;
import net.michaeljackson23.mineademia.networking.Client2Server;
import net.michaeljackson23.mineademia.networking.Server2Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class DevQuirkDisplay {
    //AbilitiesTick is constantly sending this info to this class, and this class is constantly run every tick indiividialy for each client.
//    public static String playerQuirk;
//    public static int abilityOne;
//    public static int abilityTwo;
    public static void register() {
        HudRenderCallback.EVENT.register((draw, tick) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client != null) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                int x = width/5;
                int y = height/5;

//                StateSaverAndLoader state = new StateSaverAndLoader();
//                if(state.players.containsKey(client.player.getUuid())) {
//                    playerQuirk = state.players.get(client.player.getUuid()).playerQuirk;
//                }
                TextRenderer textRenderer = client.textRenderer;
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Quirk: " + DevHudElements.quirk),
                        x, y, 0xff0000);
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Stamina: " + DevHudElements.stamina),
                        x, y + 20, 0xff0000);
                draw.drawCenteredTextWithShadow(textRenderer,
                        Text.literal("Cooldown: " + DevHudElements.cooldown),
                        x, y + 40, 0xff0000);
//                y = y + 15;
//
//                draw.drawCenteredTextWithShadow(textRenderer,
//                        Text.literal("Ability 1: " + playerData.quirkAbilityTimers[0]),
//                        x, y, 0xff0000);
//                y = y + 15;
//
//                draw.drawCenteredTextWithShadow(textRenderer,
//                        Text.literal("Ability 2: " + playerData.quirkAbilityTimers[1]),
//                        x, y, 0xff0000);
            }
        });
    }
}


