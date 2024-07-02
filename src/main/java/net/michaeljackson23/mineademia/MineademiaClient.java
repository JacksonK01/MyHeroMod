package net.michaeljackson23.mineademia;

import net.fabricmc.api.ClientModInitializer;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.armor.CustomArmorModelRenderer;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.hud.QuirkHud;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.quirk.ClientQuirkTicks;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;
import net.minecraft.client.MinecraftClient;

public class MineademiaClient implements ClientModInitializer {

    //Only client related elements go here. Such as actions on keybind press or entity rendering.
    @Override
    public void onInitializeClient() {
        MinecraftClient client = MinecraftClient.getInstance();
        Networking.registerClient();
        ClientQuirkTicks.registerClientTicks();
        Keybinds.keysRegister();
        CustomArmorModelRenderer.register();
        QuirkFeatureRenderer.register();
        BlockRegister.render();
        Keybinds.keybindActions();
        EntityRegister.registerModels();
        QuirkHud.register();
        ParticleRegister.registerClient();
        AnimationRegister.register();
    }
}
