package net.michaeljackson23.mineademia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.items.ModItems;
import net.minecraft.block.enums.Thickness;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.QUIRK_ICE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.LASTING_QUIRK_ICE);
        registerQuirkIceSpike(blockStateModelGenerator);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.QUIRK_TABLET, Models.GENERATED);
    }
    private void registerQuirkIceSpike(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateVariantMap.SingleProperty<Thickness> property = BlockStateVariantMap.create(Properties.THICKNESS);
        for (Thickness thickness : Thickness.values()) {
            property.register(thickness, getQuirkIceSpikeVariant(thickness, blockStateModelGenerator));
        }
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(BlockRegister.QUIRK_ICE_SPIKE).coordinate(property));
    }

    public final BlockStateVariant getQuirkIceSpikeVariant(Thickness thickness, BlockStateModelGenerator blockStateModelGenerator) {
        String string = "_" + thickness.asString();
        TextureMap textureMap = TextureMap.cross(TextureMap.getSubId(BlockRegister.QUIRK_ICE_SPIKE, string));
        return BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(BlockRegister.QUIRK_ICE_SPIKE, string, textureMap, blockStateModelGenerator.modelCollector));
    }
}
