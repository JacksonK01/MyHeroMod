package net.michaeljackson23.mineademia.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.blocks.quirkice.QuirkIceBlock;
import net.michaeljackson23.mineademia.blocks.quirkice.QuirkIceSpikeBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegister {
    public static final QuirkIceBlock QUIRK_ICE = new QuirkIceBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).slipperiness(0.98f).ticksRandomly().strength(0.5f).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(Blocks::never), 100);
    public static final QuirkIceBlock LASTING_QUIRK_ICE = new QuirkIceBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).slipperiness(0.98f).ticksRandomly().strength(0.5f).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(Blocks::never), 200);
    public static final QuirkIceSpikeBlock QUIRK_ICE_SPIKE = new QuirkIceSpikeBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).slipperiness(0.98f).ticksRandomly().strength(0.5f).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(Blocks::never));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(Mineademia.MOD_ID, "quirk_ice"), QUIRK_ICE);
        Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "quirk_ice"), new BlockItem(QUIRK_ICE, new FabricItemSettings()));
        Registry.register(Registries.BLOCK, new Identifier(Mineademia.MOD_ID, "lasting_quirk_ice"), LASTING_QUIRK_ICE);
        Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "lasting_quirk_ice"), new BlockItem(LASTING_QUIRK_ICE, new FabricItemSettings()));
        Registry.register(Registries.BLOCK, new Identifier(Mineademia.MOD_ID, "quirk_ice_spike"), QUIRK_ICE_SPIKE);
        Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "quirk_ice_spike"), new BlockItem(QUIRK_ICE_SPIKE, new FabricItemSettings()));
    }

    public static void render() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.QUIRK_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.LASTING_QUIRK_ICE, RenderLayer.getTranslucent());
    }
}

