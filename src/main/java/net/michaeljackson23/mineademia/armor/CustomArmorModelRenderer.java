package net.michaeljackson23.mineademia.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.deku.gamma.model.GammaSuitModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class CustomArmorModelRenderer {

    public static void register() {
        registerArmorRenderers("gamma_suit", GammaSuitModel::new, ArmorRegister.GAMMA_SUIT_HELMET, ArmorRegister.GAMMA_SUIT_CHESTPLATE, ArmorRegister.GAMMA_SUIT_LEGGINGS, ArmorRegister.GAMMA_SUIT_BOOTS);
    }

    private static void registerArmorRenderers(String nameOfModel, Function<ModelPart, AbstractArmorModel<LivingEntity>> modelConstructor, ItemConvertible... items) {
        Identifier modelTexture = new Identifier(Mineademia.MOD_ID, "/textures/armor/" + nameOfModel + ".png");
        Identifier model_id = new Identifier(Mineademia.MOD_ID, nameOfModel);
        EntityModelLayer modelLayer = new EntityModelLayer(model_id, "main");
        EntityModelLayerRegistry.registerModelLayer(modelLayer, GammaSuitModel::getTexturedModelData);

        ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
            ModelPart modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(modelLayer);
            AbstractArmorModel<LivingEntity> armorModel = modelConstructor.apply(modelPart);
            armorModel.setHelmetVisible(slot == EquipmentSlot.HEAD);
            armorModel.setChestplateVisible(slot == EquipmentSlot.CHEST);
            armorModel.setLeggingsVisible(slot == EquipmentSlot.LEGS);
            armorModel.setBootsVisible(slot == EquipmentSlot.FEET);

            contextModel.copyBipedStateTo(armorModel);

            ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, modelTexture);
        }, items);
    }
}
