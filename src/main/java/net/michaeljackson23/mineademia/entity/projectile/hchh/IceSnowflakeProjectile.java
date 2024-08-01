package net.michaeljackson23.mineademia.entity.projectile.hchh;

import net.michaeljackson23.mineademia.abilitiestest.usage.abilities.IceSnowflakeAbility;
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
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IceSnowflakeProjectile extends ThrownItemEntity {
    int timer = 0;
    int stage = 0;
    public IceSnowflakeProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public IceSnowflakeProjectile(World world, LivingEntity owner) {
        super(EntityRegister.ICE_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public IceSnowflakeProjectile(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public IceSnowflakeProjectile(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
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
    @Override
    public void tick() {
        super.tick();
        if(timer >= 10 || (timer>=3 && stage == 0)) {
            if(stage < 2){
                for(int i = 0; i<(stage+1)*20; i++){
                    IceSnowflakeProjectile iceProjectile = new IceSnowflakeProjectile(getWorld(), (LivingEntity)getOwner());
                    iceProjectile.setVelocity(this, getPitch(), getYaw(), 0f, 0.25f, random.nextBetween(10,50)*(stage+1));
                    iceProjectile.setPosition(getX(), getY(), getZ());
                    iceProjectile.stage=stage+1;
                    getWorld().spawnEntity(iceProjectile);
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
        if(state.isAir()|| state.isLiquid()){
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
