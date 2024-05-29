package net.michaeljackson23.mineademia.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

//Purely for the mixin
public class CustomArmorItem extends ArmorItem {

    public CustomArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

}
