package net.michaeljackson23.mineademia.util;

import net.michaeljackson23.mineademia.damagetypes.CustomDamageTypes;
import net.minecraft.entity.LivingEntity;

public class QuirkDamage {
    public static void doDamage(LivingEntity attacker, LivingEntity attacked, float amount) {
        attacked.setAttacker(attacker);
        attacked.damage(CustomDamageTypes.of(attacker.getWorld(), CustomDamageTypes.QUIRK_DAMAGE), amount);
    }

    public static void selfDamage(LivingEntity attacked, float amount) {
        attacked.damage(CustomDamageTypes.of(attacked.getWorld(), CustomDamageTypes.QUIRK_DAMAGE), amount);
    }
}
