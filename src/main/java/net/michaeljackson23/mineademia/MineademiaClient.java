package net.michaeljackson23.mineademia;

import net.fabricmc.api.ClientModInitializer;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.armor.CustomArmorModelRenderer;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.hud.DevHudElements;
import net.michaeljackson23.mineademia.hud.DevQuirkDisplay;
import net.michaeljackson23.mineademia.networking.Server2Client;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;

public class MineademiaClient implements ClientModInitializer {

    //Only client related elements go here. Such as actions on keybind press or entity rendering.
    @Override
    public void onInitializeClient() {
        Server2Client.registerServerToClientPackets();
        CustomArmorModelRenderer.register();
        QuirkFeatureRenderer.register();
        BlockRegister.render();
        Keybinds.keybindActions();
        EntityRegister.registerModels();
        DevQuirkDisplay.register();
        ParticleRegister.registerClient();
        AnimationRegister.register();
        DevHudElements.UpdateHudElements();
    }
}
