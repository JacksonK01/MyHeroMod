package net.michaeljackson23.mineademia.quirk.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.quirk.feature.models.EnginesModelState;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;

import java.util.HashMap;

@Environment(value= EnvType.CLIENT)
public class QuirkFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public static final HashMap<String, ModelDataHolder> modelMap = new HashMap<>();

    public QuirkFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
        MinecraftClient client = MinecraftClient.getInstance();
        //Registry isn't registered yet which is why modelData has a constructor and init method
        modelMap.forEach(((name, modelDataHolder) -> {
            modelDataHolder.modelInit(client);
        }));
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        matrices.push();
        if(player instanceof QuirkDataHelper quirkPlayer) {
            QuirkData quirkData = quirkPlayer.myHeroMod$getQuirkData();
            //player.sendMessage(Text.literal("[" + player.getName().getString() + "] " + quirkData));

            for(int i = 0; i < quirkData.getModelsArrayLength(); i++) {
                String model = quirkData.getModel(i);
                //player.sendMessage(Text.literal("Processing Model -> " + model));
                if(modelMap.containsKey(model)) {
                    ModelDataHolder modelDataHolder =  modelMap.get(model);
                    modelDataHolder.process(player);
                    VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(modelDataHolder.getTexture()));
                    this.getContextModel().copyBipedStateTo(modelDataHolder.getModel());
                    modelDataHolder.getModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        matrices.pop();
    }
    public static void register() {
        modelMap.put("EnginesAndEngineFire", new ModelDataHolder("engines", "textures/features/engines_model.png", EnginesModelState::new, EnginesModelState::getTexturedModelData));

    }
}
