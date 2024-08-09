package net.michaeljackson23.mineademia.entity.projectile.stungrenade;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.PlaceParticleInWorld;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class StunGrenadeProjectile extends ThrownItemEntity {
    int timer = 0;

    public StunGrenadeProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public StunGrenadeProjectile(World world, LivingEntity owner) {
        super(EntityRegister.STUN_GRENADE_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public StunGrenadeProjectile(World world, double x, double y, double z) {
        super(EntityRegister.STUN_GRENADE_PROJECTILE, x, y, z, world);
        setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        if(timer >= 15) {
            kill();
            return;
        }
        timer++;
        aoe();
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.FLASH, getX(), getY(), getZ(), 0, 0, 0, 1);
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.LAVA, getX(), getY(), getZ(), 0.3, 0.3, 0.3, 3);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        aoe();
        kill();
    }

    @Override
    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        aoe();
        this.kill();
    }

    private void aoe() {
        if(getOwner() == null && !(getOwner() instanceof LivingEntity)) {
            return;
        }
        AreaOfEffect.execute((LivingEntity) getOwner(), 4, 1, getX(), getY(), getZ(), (entityToEffect) -> {
            entityToEffect.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 2, true, false));
            entityToEffect.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 25, 2, true, false));
            entityToEffect.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 25, 2, true, false));
            QuirkDamage.doEmitterDamage((LivingEntity) getOwner(), entityToEffect, 1f);
        });
    }
}
