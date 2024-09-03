package net.michaeljackson23.mineademia.entity.projectile.apshot;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.util.PlaceClientParticleInWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class APShotProjectile extends ThrownItemEntity {
    int timer = 0;

    public APShotProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public APShotProjectile(World world, LivingEntity owner) {
        super(EntityRegister.AP_SHOT_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public APShotProjectile(World world, double x, double y, double z) {
        super(EntityRegister.AP_SHOT_PROJECTILE, x, y, z, world);
        setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        if(timer % 5 == 0) {
            PlaceClientParticleInWorld.spawn(getWorld(), ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 0, 0, 0, 1);
        }
        if(timer >= 50) {
            kill();
            return;
        }
        timer++;
        PlaceClientParticleInWorld.spawn(getWorld(), ParticleTypes.LAVA, getX(), getY(), getZ(), 0, 0, 0, 1);
        PlaceClientParticleInWorld.spawn(getWorld(), ModParticles.QUIRK_EXPLOSION_SHORT, getX(), getY(), getZ(), 0.3, 0.3, 0.3, 2);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            this.getWorld().createExplosion(this.getOwner(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0.5f, World.ExplosionSourceType.TNT);
        }
        kill();
    }

    @Override
    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 0.5f, World.ExplosionSourceType.TNT);
        this.kill();
    }
}
