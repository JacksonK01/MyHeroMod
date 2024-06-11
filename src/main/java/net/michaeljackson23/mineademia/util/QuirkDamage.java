package net.michaeljackson23.mineademia.util;

import net.michaeljackson23.mineademia.damagetypes.CustomDamageTypes;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;

public class QuirkDamage {
    public static void doDamage(LivingEntity attacker, LivingEntity attacked, float amount) {
        attacked.setAttacker(attacker);
        attacked.damage(CustomDamageTypes.of(attacker.getWorld(), CustomDamageTypes.QUIRK_DAMAGE), amount);
    }

    public static void selfDamage(LivingEntity attacked, float amount) {
        attacked.damage(CustomDamageTypes.of(attacked.getWorld(), CustomDamageTypes.QUIRK_DAMAGE), amount);
    }

    public static void doEmitterDamage(LivingEntity attacker, LivingEntity attacked, float amount) {
        attacked.setAttacker(attacker);
        attacked.damage(CustomDamageTypes.of(attacker.getWorld(), CustomDamageTypes.EMITTER_QUIRK_DAMAGE), amount);
    }

    public static void doPhysicalDamage(LivingEntity attacker, LivingEntity attacked, float amount) {
        attacked.setAttacker(attacker);
        if(attacked.damage(CustomDamageTypes.of(attacker.getWorld(), CustomDamageTypes.PHYSICAL_QUIRK_DAMAGE), amount)) {
            attacked.getWorld().playSound(null, attacked.getBlockPos(), CustomSounds.PHYSICAL_DAMAGE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
}
