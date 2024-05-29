package net.michaeljackson23.mineademia.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.blocks.quirkice.QuirkIceBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegister {
    public static final QuirkIceBlock QUIRK_ICE_BLOCK = new QuirkIceBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).slipperiness(0.98f).ticksRandomly().strength(0.5f).sounds(BlockSoundGroup.GLASS).nonOpaque().solidBlock(Blocks::never));

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(Mineademia.Mod_id, "quirk_ice"), QUIRK_ICE_BLOCK);
        Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "quirk_ice"), new BlockItem(QUIRK_ICE_BLOCK, new FabricItemSettings()));
    }

    public static void render() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegister.QUIRK_ICE_BLOCK, RenderLayer.getTranslucent());
    }
}
