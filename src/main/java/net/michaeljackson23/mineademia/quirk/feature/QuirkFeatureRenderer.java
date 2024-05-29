package net.michaeljackson23.mineademia.quirk.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.feature.models.EnginesModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class QuirkFeatureRenderer <T extends LivingEntity, M extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {

    EnginesModel<T> enginesModel;
    FeatureRendererContext<T, M> playerContext;
    public static final Identifier ENGINE_TEXTURE = new Identifier(Mineademia.Mod_id, "textures/features/engines_model.png");
    public static final Identifier ENGINES_ID = new Identifier(Mineademia.Mod_id, "engines");
    public static final EntityModelLayer ENGINES_LAYER = new EntityModelLayer(ENGINES_ID, "main");

    public QuirkFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
        ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(ENGINES_LAYER);
        enginesModel = new EnginesModel<>(modelPart);

        playerContext = context;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        enginesModel.setEnginesVisible(true);
        enginesModel.setEnginesFireVisible(false);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ENGINE_TEXTURE));
        playerContext.getModel().copyBipedStateTo(enginesModel);
        enginesModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void getTexture() {

    }

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(ENGINES_LAYER, EnginesModel::getTexturedModelData);
    }
}
