package net.michaeljackson23.mineademia.quirk.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.hud.DevHudElements;
import net.michaeljackson23.mineademia.quirk.feature.models.EnginesModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
@SuppressWarnings("unchecked")
@Environment(value= EnvType.CLIENT)
public class QuirkFeatureRenderer <T extends LivingEntity, M extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {
    FeatureRendererContext<T, M> playerContext;
    public static final HashMap<String, ModelData> modelList = new HashMap<>();
    private static final ArrayList<ModelData> activeModels = new ArrayList<>();

    public QuirkFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        playerContext = context;
        MinecraftClient client = MinecraftClient.getInstance();
        //Registry isn't registered yet which is why modelData has a constructor and init method
        modelList.forEach(((name, modelData) -> {
            modelData.modelInit(client);
        }));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(!activeModels.isEmpty()) {
            activeModels.forEach((modelData) -> {
                if(modelData.getModel() instanceof EnginesModel<?> enginesModel) {
                    enginesModel.setEnginesVisible(true);
                }
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(modelData.getTexture()));
                playerContext.getModel().copyBipedStateTo((BipedEntityModel<T>) modelData.getModel());
                modelData.getModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
            });
        }
   }

    public static void register() {
        modelList.put("Electrification", new ModelData("engines", "textures/features/engines_model.png", EnginesModel::new, EnginesModel::getTexturedModelData));

    }

    public static void activateModel(String modelName) {
        activeModels.add(modelList.get(modelName));
    }

    public static void deActivateModel(String modelName) {
        activeModels.remove(modelList.get(modelName));
    }

    public static void deActivateAllModels() {
        activeModels.clear();
    }
}
