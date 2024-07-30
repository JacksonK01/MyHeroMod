package net.michaeljackson23.mineademia.statuseffects;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatusEffectsRegister {
    public static CowlingStatusEffect EFFECT_COWLING = new CowlingStatusEffect();
    public static FrozenStatusEffect EFFECT_FROZEN = new FrozenStatusEffect();
    static {
        EFFECT_COWLING.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635", 0.2f,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        EFFECT_COWLING.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.5,
                EntityAttributeModifier.Operation.ADDITION);
        EFFECT_FROZEN.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                "7107DE5E-7CE8-4030-940E-514C1F160890", -0.1,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Mineademia.MOD_ID, "cowling"), EFFECT_COWLING);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Mineademia.MOD_ID, "frozen"), EFFECT_FROZEN);
    }
}
