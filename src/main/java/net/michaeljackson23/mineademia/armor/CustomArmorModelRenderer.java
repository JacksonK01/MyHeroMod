package net.michaeljackson23.mineademia.armor;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.deku.beta.model.DekuBetaCostume;
import net.michaeljackson23.mineademia.armor.deku.gamma.model.DekuGammaCostume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public class CustomArmorModelRenderer {

    public static void register() {
        registerArmorRenderers("deku_gamma_costume", DekuGammaCostume::new, DekuGammaCostume::getTexturedModelData, ArmorRegister.GAMMA_SUIT_HELMET, ArmorRegister.GAMMA_SUIT_CHESTPLATE, ArmorRegister.GAMMA_SUIT_LEGGINGS, ArmorRegister.GAMMA_SUIT_BOOTS);
        registerArmorRenderers("deku_beta_costume", DekuBetaCostume::new, DekuBetaCostume::getTexturedModelData, ArmorRegister.BETA_SUIT_HELMET, ArmorRegister.BETA_SUIT_CHESTPLATE, ArmorRegister.BETA_SUIT_LEGGINGS, ArmorRegister.BETA_SUIT_BOOTS);
    }

    private static void registerArmorRenderers(String nameOfModel, Function<ModelPart, AbstractArmorModel<LivingEntity>> modelConstructor, EntityModelLayerRegistry.TexturedModelDataProvider provider, ItemConvertible... items) {
        Identifier modelTexture = new Identifier(Mineademia.MOD_ID, "/textures/armor/" + nameOfModel + ".png");
        Identifier model_id = new Identifier(Mineademia.MOD_ID, nameOfModel);
        EntityModelLayer modelLayer = new EntityModelLayer(model_id, "main");
        EntityModelLayerRegistry.registerModelLayer(modelLayer, provider);

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
