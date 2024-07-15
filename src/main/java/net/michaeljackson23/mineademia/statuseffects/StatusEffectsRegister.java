package net.michaeljackson23.mineademia.statuseffects;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatusEffectsRegister {
    public static CowlingStatusEffect COWLING_STATUS_EFFECT = new CowlingStatusEffect();
    static {
        COWLING_STATUS_EFFECT.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635", 0.2f,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        COWLING_STATUS_EFFECT.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.5,
                EntityAttributeModifier.Operation.ADDITION);
    }

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Mineademia.MOD_ID, "cowling"), COWLING_STATUS_EFFECT);
    }
}
