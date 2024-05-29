package net.michaeljackson23.mineademia.armor;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.deku.gamma.GammaMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArmorRegister {
    public static final GammaMaterial GAMMA_MATERIAL = new GammaMaterial();
    public static final Item GAMMA_SUIT_HELMET = new CustomArmorItem(GAMMA_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item GAMMA_SUIT_CHESTPLATE = new CustomArmorItem(GAMMA_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings());
    public static final Item GAMMA_SUIT_LEGGINGS = new CustomArmorItem(GAMMA_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final Item GAMMA_SUIT_BOOTS = new CustomArmorItem(GAMMA_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());


    public static void register() {
        addToItemRegistry("gamma_suit_helmet", GAMMA_SUIT_HELMET);
        addToItemRegistry("gamma_suit_chestplate", GAMMA_SUIT_CHESTPLATE);
        addToItemRegistry("gamma_suit_leggings", GAMMA_SUIT_LEGGINGS);
        addToItemRegistry("gamma_suit_boots", GAMMA_SUIT_BOOTS);
    }

    private static void addToItemRegistry(String id, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Mineademia.Mod_id, id), item);
    }
}
