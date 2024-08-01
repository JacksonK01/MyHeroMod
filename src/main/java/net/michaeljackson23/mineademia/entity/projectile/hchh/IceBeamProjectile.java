package net.michaeljackson23.mineademia.entity.projectile.hchh;

import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.statuseffects.QuirkEffectUtil;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IceBeamProjectile extends ThrownItemEntity {
    int timer = 0;
    int stage = 0;
    public IceBeamProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public IceBeamProjectile(World world, LivingEntity owner) {
        super(EntityRegister.ICE_SNOWFLAKE_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public IceBeamProjectile(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public IceBeamProjectile(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return true;
    }
    @Override
    protected Item getDefaultItem() {
        return Blocks.AIR.asItem();
    }
    private IceBeamProjectile createNewProjectile(int currentStage){
        IceBeamProjectile iceProjectile = new IceBeamProjectile(getWorld(), (LivingEntity)getOwner());
        iceProjectile.setVelocity(getVelocity());
        iceProjectile.stage=currentStage;
        return iceProjectile;
    }
    @Override
    public void tick() {
        super.tick();
        if(timer >= 5) {
            if(stage < 3){
                    double pitch = ((getPitch() + 90) * Math.PI) / 180;
                    double yaw = ((getYaw() + 90) * Math.PI)/ 180;
                    double x = Math.cos(yaw) ;
                    double y = Math.sin(pitch);
                    double z = Math.sin(yaw);
                    for(int i = -1 ; i<=1; i+=2)
                        for(int j = -1; j<=1 ; j+=2)
                            for(int k = -1; k<=1; k+=2) {
                                IceBeamProjectile projectile = createNewProjectile(stage + 1);
                                projectile.setPosition(getX() + z * 0.5 * i, getY() + y * 0.5 * j, getZ() - x * 0.5 * k);
                                getWorld().spawnEntity(projectile);
                            }


            }
            kill();
            return;
        }
        if(!(getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        timer++;
        var pos = new BlockPos((int) prevX, (int) prevY, (int) prevZ);
        var state = serverWorld.getBlockState(pos);
        if((state.isAir()|| state.isLiquid()) && !(stage==0 && age<3)){
            serverWorld.setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
        }

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if(getOwner() != entityHitResult.getEntity()) {
            if (getOwner() instanceof LivingEntity owner && entityHitResult.getEntity() instanceof LivingEntity hitEntity) {
                QuirkDamage.doEmitterDamage(owner, hitEntity, 2f);
                QuirkEffectUtil.applyFrozen(hitEntity, 20);
            }
            kill();
        }
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if(getWorld() instanceof ServerWorld serverWorld) {
            BlockPos hitSpot = new BlockPos((int) hitResult.getPos().getX() - 1, (int) hitResult.getPos().getY(), (int) hitResult.getPos().getZ());
            if(serverWorld.isWater(hitSpot))
                serverWorld.setBlockState(hitSpot, BlockRegister.QUIRK_ICE.getDefaultState());
            if(hitResult.getType() == HitResult.Type.ENTITY){
                EntityHitResult entityHitResult = (EntityHitResult)hitResult;
                if(entityHitResult.getEntity() == getOwner())
                    return;
            }
            if(hitResult.getType() == HitResult.Type.BLOCK){
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                if(getWorld().getBlockState(blockHitResult.getBlockPos()).isOf(BlockRegister.QUIRK_ICE))
                    return;
            }
        }
        this.kill();

    }
}
