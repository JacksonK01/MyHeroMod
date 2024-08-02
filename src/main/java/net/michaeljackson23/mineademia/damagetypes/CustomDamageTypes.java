package net.michaeljackson23.mineademia.damagetypes;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CustomDamageTypes {

    public static final RegistryKey<DamageType> QUIRK_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Mineademia.MOD_ID, "quirk_damage_type"));
    public static final RegistryKey<DamageType> EMITTER_QUIRK_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Mineademia.MOD_ID, "quirk_emitter_damage_type"));
    public static final RegistryKey<DamageType> PHYSICAL_QUIRK_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Mineademia.MOD_ID, "quirk_physical_damage_type"));

    public static final RegistryKey<DamageType> QUIRK_EXPLOSION_AP_SHOT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Mineademia.MOD_ID, "quirk_explosion_ap_shot_damage_type"));


    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

}
