package net.michaeljackson23.mineademia.armor;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.deku.beta.BetaMaterial;
import net.michaeljackson23.mineademia.armor.deku.gamma.GammaMaterial;
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

    public static final BetaMaterial BETA_MATERIAL = new BetaMaterial();
    public static final Item BETA_SUIT_HELMET = new CustomArmorItem(BETA_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item BETA_SUIT_CHESTPLATE = new CustomArmorItem(BETA_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings());
    public static final Item BETA_SUIT_LEGGINGS = new CustomArmorItem(BETA_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final Item BETA_SUIT_BOOTS = new CustomArmorItem(BETA_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());


    public static void register() {
        addToItemRegistry("gamma_suit_helmet", GAMMA_SUIT_HELMET);
        addToItemRegistry("gamma_suit_chestplate", GAMMA_SUIT_CHESTPLATE);
        addToItemRegistry("gamma_suit_leggings", GAMMA_SUIT_LEGGINGS);
        addToItemRegistry("gamma_suit_boots", GAMMA_SUIT_BOOTS);

        //TODO make this have unique item textures
        addToItemRegistry("beta_suit_helmet", BETA_SUIT_HELMET);
        addToItemRegistry("beta_suit_chestplate", BETA_SUIT_CHESTPLATE);
        addToItemRegistry("beta_suit_leggings", BETA_SUIT_LEGGINGS);
        addToItemRegistry("beta_suit_boots", BETA_SUIT_BOOTS);
    }

    private static void addToItemRegistry(String id, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Mineademia.MOD_ID, id), item);
    }
}
