package net.michaeljackson23.mineademia;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityEvents;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.armor.CustomArmorModelRenderer;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.client.hud.QuirkHud;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.client.keybinds.Keybinds;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.quirk.ClientQuirkTicks;
import net.michaeljackson23.mineademia.quirk.feature.QuirkFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;

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
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.QUIRK_ICE_SPIKE, RenderLayer.getCutout());
        Keybinds.keybindActions();
        EntityRegister.registerModels();
        ParticleRegister.registerClient();
        AnimationRegister.register();

        registerEvents();
    }

    private void registerEvents() {
        HudRenderCallback.EVENT.register(QuirkHud::display);
    }
}
