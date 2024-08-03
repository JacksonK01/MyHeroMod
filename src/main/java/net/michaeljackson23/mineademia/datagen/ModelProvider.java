package net.michaeljackson23.mineademia.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.blocks.quirkice.QuirkIceSpikeBlock;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.QUIRK_ICE);
        blockStateModelGenerator.registerSimpleCubeAll(BlockRegister.LASTING_QUIRK_ICE);
        blockStateModelGenerator.registerTintableCrossBlockStateWithStages(BlockRegister.QUIRK_ICE_SPIKE, BlockStateModelGenerator.TintType.NOT_TINTED, QuirkIceSpikeBlock.MAX_GROWTH, 0, 1, 2, 3, 4, 5);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemRegister.quirkTablet, Models.GENERATED);
    }
}
