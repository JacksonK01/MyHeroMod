package net.michaeljackson23.mineademia.quirk.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Environment(value=EnvType.CLIENT)
public class ModelData implements QuirkModelLogicHelper {
    private final Identifier id;
    private final Identifier texture;
    private final EntityModelLayer layer;
    private BipedEntityModel<AbstractClientPlayerEntity> model;
    private final Function<ModelPart, BipedEntityModel<AbstractClientPlayerEntity>> modelConstructor;

    public ModelData(String modelName, String texture, Function<ModelPart, BipedEntityModel<AbstractClientPlayerEntity>> constructor, EntityModelLayerRegistry.TexturedModelDataProvider texturedModelData) {
        this.id = new Identifier(Mineademia.MOD_ID, modelName);
        this.texture = new Identifier(Mineademia.MOD_ID, texture);
        this.layer = new EntityModelLayer(id, "main");
        EntityModelLayerRegistry.registerModelLayer(layer, texturedModelData);
        modelConstructor = constructor;
    }
    //Registry isn't registered yet which is why modelData has a constructor and init method
    public void modelInit(MinecraftClient client) {
        ModelPart modelPart = client.getEntityModelLoader().getModelPart(layer);
        model = modelConstructor.apply(modelPart);
    }

    public Identifier getId() {
        return this.id;
    }

    public Identifier getTexture() {
        return this.texture;
    }

    public EntityModelLayer getLayer() {
        return this.layer;
    }

    public BipedEntityModel<AbstractClientPlayerEntity> getModel() {
        return this.model;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj instanceof ModelData modelData) {
            return this.id.equals(modelData.getId());
        }
        return false;
    }

    @Override
    public void process(PlayerEntity player) {
        if(model instanceof QuirkModelLogicHelper entityModel) {
            entityModel.process(player);
        }
    }
}
