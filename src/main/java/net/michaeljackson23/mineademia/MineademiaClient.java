package net.michaeljackson23.mineademia;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.michaeljackson23.mineademia.animations.AnimationRegister;
import net.michaeljackson23.mineademia.hud.DevHudElements;
import net.michaeljackson23.mineademia.hud.DevQuirkDisplay;
import net.michaeljackson23.mineademia.networking.Server2Client;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.entity.cube.CubeEntityModel;
import net.michaeljackson23.mineademia.entity.cube.CubeEntityRenderer;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class MineademiaClient implements ClientModInitializer {

    //Only client related elements go here. Such as actions on keybind press or entity rendering.
    @Override
    public void onInitializeClient() {
        Server2Client.registerServerToClientPackets();
        Keybinds.keybindActions();

        EntityRendererRegistry.register(EntityRegister.CUBE, CubeEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CubeEntityRenderer.MODEL_CUBE_LAYER, CubeEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(EntityRegister.AIR_FORCE_PROJECTILE, FlyingItemEntityRenderer::new);

//        EntityRendererRegistry.register(EntityRegister.CUBE, (context) -> {
//            return new CubeEntityRenderer(context);
//        });
//        EntityModelLayerRegistry.registerModelLayer(CubeEntityRenderer.MODEL_CUBE_LAYER, CubeEntityModel::getTexturedModelData);
//
//        EntityRendererRegistry.register(EntityRegister.AIR_FORCE_PROJECTILE, (context) -> {
//            return new FlyingItemEntityRenderer<>(context);
//        });

        DevQuirkDisplay.register();
        ParticleRegister.registerClient();
        AnimationRegister.register();
        DevHudElements.UpdateHudElements();
    }
}
