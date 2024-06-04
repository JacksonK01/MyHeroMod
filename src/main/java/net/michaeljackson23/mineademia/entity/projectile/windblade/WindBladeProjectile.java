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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WindBladeProjectile extends ThrownItemEntity {
    int timer = 0;
    Vec3d storedDirection;
    boolean hasCollision = false;

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    public WindBladeProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public WindBladeProjectile(World world, LivingEntity owner) {
        super(EntityRegister.WIND_BLADE_PROJECTILE, owner, world);
    }

    public WindBladeProjectile(World world, double x, double y, double z) {
        super(EntityRegister.WIND_BLADE_PROJECTILE, x, y, z, world);
    }

    public void tick() {
        super.tick();
        if(getOwner() == null) {
            kill();
            return;
        }
        if(hasCollision) {
            kill();
            return;
        }
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.SWEEP_ATTACK, getX(), getY(), getZ(), 0.3, 0.3, 0.3, 3);
        PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.4, 0.4, 0.4, 2);
        this.setNoGravity(true);
        Vec3d ownerVec = getLivingEntityOwner().getRotationVec(1.0f).multiply(1.2);
        this.setVelocity(ownerVec);
        AreaOfEffect.execute(getLivingEntityOwner(), 4, 2, getX(), getY(), getZ(), (entityToAffect) -> {
            entityToAffect.setVelocity(ownerVec);
            entityToAffect.velocityModified = true;
            PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.SWEEP_ATTACK, entityToAffect.getX(), entityToAffect.getY(), entityToAffect.getZ(), 0.3, 0.3, 0.3, 3);
            QuirkDamage.doDamage(getLivingEntityOwner(), entityToAffect, 1f);
        });

        timer++;
        if(timer > 45) {
            this.kill();
        }
    }

    public LivingEntity getLivingEntityOwner() {
        return (LivingEntity) getOwner();
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        if(getOwner() == null) {
            kill();
            return;
        }
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.PLAYERS, 3f, 2f);
            Vec3d ownerVec = getLivingEntityOwner().getRotationVec(1.0f).multiply(-1, 1, -1);
            AreaOfEffect.execute(getLivingEntityOwner(), 4, 2, getX(), getY(), getZ(), (entityToAffect) -> {
                PlaceParticleInWorld.spawn(getWorld(), ParticleTypes.EXPLOSION, entityToAffect.getX(), entityToAffect.getY(), entityToAffect.getZ(), 0.3, 0.3, 0.3, 3);
                entityToAffect.setVelocity(ownerVec);
                entityToAffect.velocityModified = true;
                QuirkDamage.doDamage(getLivingEntityOwner(), entityToAffect, 5f);
            });
            hasCollision = true;
            kill();
        }
    }
}

