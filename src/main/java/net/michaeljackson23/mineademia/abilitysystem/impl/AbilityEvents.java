package net.michaeljackson23.mineademia.abilitysystem.impl;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public final class AbilityEvents {

    private AbilityEvents() { }


    public static boolean onEntityDamage(LivingEntity entity, DamageSource damageSource, float v) {
        AbilityManager.triggerEvents(ServerLivingEntityEvents.AllowDamage.class);
        return true;
    }

    public static void onEntityDeath(LivingEntity entity, DamageSource damageSource) {

    }

}
