package net.michaeljackson23.mineademia.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.ArmorRegister;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.items.custom.MockQuirkTablet;
import net.michaeljackson23.mineademia.items.custom.QuirkTablet;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegister {
    public static Item sharpShooter;
    public static QuirkTablet quirkTablet;
    public static MockQuirkTablet mockQuirkTablet;

    public static void register() {
        ItemRegister.quirkTablet = Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "quirk_menu_selector"), new QuirkTablet(new FabricItemSettings().maxCount(1)));
        ItemRegister.mockQuirkTablet = Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "mock_quirk_tablet"), new MockQuirkTablet(new FabricItemSettings().maxCount(1)));
        ItemRegister.sharpShooter = Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, "sharp_shooter"), new Item(new FabricItemSettings().maxCount(1)));


        ItemGroup MHA_GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(quirkTablet))
                .displayName(Text.translatable("itemGroup.mineademia.item_group"))
                .entries((context, entries) -> {
                    entries.add(quirkTablet);
                    entries.add(mockQuirkTablet);
                    entries.add(ItemRegister.sharpShooter);
                    entries.add(BlockRegister.QUIRK_ICE);
                    entries.add(ArmorRegister.GAMMA_SUIT_HELMET);
                    entries.add(ArmorRegister.GAMMA_SUIT_CHESTPLATE);
                    entries.add(ArmorRegister.GAMMA_SUIT_LEGGINGS);
                    entries.add(ArmorRegister.GAMMA_SUIT_BOOTS);
                    entries.add(ArmorRegister.BETA_SUIT_HELMET);
                    entries.add(ArmorRegister.BETA_SUIT_CHESTPLATE);
                    entries.add(ArmorRegister.BETA_SUIT_LEGGINGS);
                    entries.add(ArmorRegister.BETA_SUIT_BOOTS);
                })
                .build();
        Registry.register(Registries.ITEM_GROUP, new Identifier(Mineademia.MOD_ID, "mha_group"), MHA_GROUP);
    }
}
