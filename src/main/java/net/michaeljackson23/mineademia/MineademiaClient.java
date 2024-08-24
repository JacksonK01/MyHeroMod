package net.michaeljackson23.mineademia;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityDecoders;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityEncoders;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityNetworkManager;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.armor.CustomArmorModelRenderer;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.client.hud.QuirkHud;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.client.keybinds.Keybinds;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.particles.ModParticles;
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
        ModParticles.registerClient();
        AnimationRegister.register();

        registerServices();
        registerEvents();
    }

    private void registerServices() {
        AbilityNetworkManager.register();
        // AbilityDecoders.registerDecoders();
    }

    private void registerEvents() {
        HudRenderCallback.EVENT.register(QuirkHud::display);
    }
}
