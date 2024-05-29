package net.michaeljackson23.mineademia.entity.projectile.windblade;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.PlaceParticleInWorld;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WindBladeProjectile extends ThrownItemEntity {
    int timer = 0;
    LivingEntity owner;

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    public WindBladeProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public WindBladeProjectile(World world, LivingEntity owner) {
        super(EntityRegister.WIND_BLADE_PROJECTILE, owner, world);
        this.owner = owner;
    }

    public WindBladeProjectile(World world, double x, double y, double z) {
        super(EntityRegister.WIND_BLADE_PROJECTILE, x, y, z, world);
    }

    public void tick() {
        super.tick();
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.SWEEP_ATTACK, getX(), getY(), getZ(), 0.3, 0.3, 0.3, 3);
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.4, 0.4, 0.4, 2);
        this.setNoGravity(true);
        if(getOwner() != null) {
            Vec3d ownerVec = getOwner().getRotationVec(1.0f).multiply(1.2);
            this.setVelocity(ownerVec);
            AreaOfEffect.execute(getOwner(), 3, 1, getX(), getY(), getZ(), (entityToAffect) -> {
                entityToAffect.setVelocity(ownerVec);
                entityToAffect.velocityModified = true;
                QuirkDamage.doDamage(getOwner(), entityToAffect, 1f);
            });
        }
        timer++;
        if(timer > 45) {
            this.kill();
        }
    }

    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        kill();
    }
}

