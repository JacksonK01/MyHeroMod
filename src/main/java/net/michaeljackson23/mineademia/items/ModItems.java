package net.michaeljackson23.mineademia.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.ArmorRegister;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.items.custom.EnergyDrinkItem;
import net.michaeljackson23.mineademia.items.custom.MockQuirkTablet;
import net.michaeljackson23.mineademia.items.custom.QuirkTablet;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ModItems {

    public static final Item SHARP_SHOOTER = registerItem("sharp_shooter", new Item(new FabricItemSettings().maxCount(1)));
    public static final QuirkTablet QUIRK_TABLET = registerItem("quirk_menu_selector", new QuirkTablet(new FabricItemSettings().maxCount(1)));
    public static final MockQuirkTablet MOCK_QUIRK_TABLET = registerItem("mock_quirk_tablet", new MockQuirkTablet(new FabricItemSettings().maxCount(1)));

    public static final Item ENERGY_DRINK = registerItem("energy_drink", new EnergyDrinkItem());


    public static final ItemGroup MHA_GROUP = registerItemGroup("mha_group", FabricItemGroup.builder().icon(() -> new ItemStack(QUIRK_TABLET)).displayName(Text.translatable("itemGroup.mineademia.item_group")).entries((context, entries) -> {
        entries.add(QUIRK_TABLET);
        entries.add(MOCK_QUIRK_TABLET);
        entries.add(SHARP_SHOOTER);

        entries.add(ENERGY_DRINK);

        entries.add(BlockRegister.QUIRK_ICE);
        entries.add(ArmorRegister.GAMMA_SUIT_HELMET);
        entries.add(ArmorRegister.GAMMA_SUIT_CHESTPLATE);
        entries.add(ArmorRegister.GAMMA_SUIT_LEGGINGS);
        entries.add(ArmorRegister.GAMMA_SUIT_BOOTS);
        entries.add(ArmorRegister.BETA_SUIT_HELMET);
        entries.add(ArmorRegister.BETA_SUIT_CHESTPLATE);
        entries.add(ArmorRegister.BETA_SUIT_LEGGINGS);
        entries.add(ArmorRegister.BETA_SUIT_BOOTS);
    }).build());


    public static void register() {


//        ItemGroup MHA_GROUP = FabricItemGroup.builder()
//                .icon(() -> new ItemStack(quirkTablet))
//                .displayName(Text.translatable("itemGroup.mineademia.item_group"))
//                .entries((context, entries) -> {
//                    entries.add(quirkTablet);
//                    entries.add(mockQuirkTablet);
//                    entries.add(ItemRegister.sharpShooter);
//                    entries.add(BlockRegister.QUIRK_ICE);
//                    entries.add(ArmorRegister.GAMMA_SUIT_HELMET);
//                    entries.add(ArmorRegister.GAMMA_SUIT_CHESTPLATE);
//                    entries.add(ArmorRegister.GAMMA_SUIT_LEGGINGS);
//                    entries.add(ArmorRegister.GAMMA_SUIT_BOOTS);
//                    entries.add(ArmorRegister.BETA_SUIT_HELMET);
//                    entries.add(ArmorRegister.BETA_SUIT_CHESTPLATE);
//                    entries.add(ArmorRegister.BETA_SUIT_LEGGINGS);
//                    entries.add(ArmorRegister.BETA_SUIT_BOOTS);
//                })
//                .build();
//        Registry.register(Registries.ITEM_GROUP, new Identifier(Mineademia.MOD_ID, "mha_group"), MHA_GROUP);

        Mineademia.LOGGER.info("Registering items for " + Mineademia.MOD_ID);
    }

    public static <T extends ItemGroup> T registerItemGroup(@NotNull String id, @NotNull T group) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(Mineademia.MOD_ID, id), group);
    }

    private static <T extends Item> T registerItem(@NotNull String id, @NotNull T item) {
        return Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, id), item);
    }

}
