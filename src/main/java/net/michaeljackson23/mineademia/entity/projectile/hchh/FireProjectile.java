package net.michaeljackson23.mineademia.entity.projectile.hchh;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireProjectile extends ThrownItemEntity {
    int timer = 0;

    public FireProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public FireProjectile(World world, LivingEntity owner) {
        super(EntityRegister.FIRE_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public FireProjectile(World world, double x, double y, double z) {
        super(EntityRegister.FIRE_PROJECTILE, x, y, z, world);
        setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        if(timer >= 100) {
            kill();
            return;
        }
        if(!(getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        timer++;
        serverWorld.spawnParticles(ParticleTypes.FLAME,
                getX(), getY(), getZ(),
                4, 0.3, 0.3, 0.3, 0.01);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        if(getOwner() instanceof LivingEntity owner && entityHitResult.getEntity() instanceof LivingEntity hitEntity) {
            QuirkDamage.doEmitterDamage(owner, hitEntity, 4f);
            hitEntity.setOnFireFor(3);
        }
        kill();
    }

    @Override
    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if(getWorld() instanceof ServerWorld serverWorld) {
            BlockPos hitSpot = new BlockPos((int) hitResult.getPos().getX() - 1, (int) hitResult.getPos().getY(), (int) hitResult.getPos().getZ());
            if(serverWorld.isAir(hitSpot)) {
                serverWorld.setBlockState(hitSpot, Blocks.FIRE.getDefaultState());
            }
        }
        this.kill();
    }
}
