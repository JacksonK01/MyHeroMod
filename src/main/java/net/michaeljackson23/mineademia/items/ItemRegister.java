package net.michaeljackson23.mineademia.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.CustomArmorMaterial;
import net.michaeljackson23.mineademia.items.custom.QuirkTablet;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemRegister {

//    public Item ItemGenerator() {
//        Item item = new Item(new FabricItemSettings());
//        Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "quirk_tablet"), item);
//        return item;
//    }
    public static Item sharpShooter;
    public static QuirkTablet quirkTablet;


    public static final ArmorMaterial CUSTOM_ARMOR_MATERIAL = new CustomArmorMaterial();

    public static Item CUSTOM_MATERIAL_HELMET;
    public static Item CUSTOM_MATERIAL_CHEST;
    public static Item CUSTOM_MATERIAL_LEGS;
    public static Item CUSTOM_MATERIAL_BOOTS;

    public static void register() {
        ItemRegister.quirkTablet = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "quirk_menu_selector"), new QuirkTablet(new FabricItemSettings().maxCount(1)));
        ItemRegister.sharpShooter = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "sharp_shooter"), new Item(new FabricItemSettings().maxCount(1)));

        ItemRegister.CUSTOM_MATERIAL_HELMET = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "custom_material_helmet"), new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()));
        ItemRegister.CUSTOM_MATERIAL_CHEST = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "custom_material_chest"), new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
        ItemRegister.CUSTOM_MATERIAL_LEGS = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "custom_material_legs"), new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()));
        ItemRegister.CUSTOM_MATERIAL_BOOTS = Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, "custom_material_boots"), new ArmorItem(CUSTOM_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()));

        ItemGroup MHA_GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(quirkTablet))
                .displayName(Text.translatable("itemGroup.mineademia.item_group"))
                .entries((context, entries) -> {
                    entries.add(quirkTablet);
                    entries.add(ItemRegister.sharpShooter);
                    entries.add(ItemRegister.CUSTOM_MATERIAL_HELMET);
                    entries.add(ItemRegister.CUSTOM_MATERIAL_CHEST);
                    entries.add(ItemRegister.CUSTOM_MATERIAL_LEGS);
                    entries.add(ItemRegister.CUSTOM_MATERIAL_BOOTS);

                })
                .build();
        Registry.register(Registries.ITEM_GROUP, new Identifier(Mineademia.Mod_id, "mha_group"), MHA_GROUP);
    }
}
