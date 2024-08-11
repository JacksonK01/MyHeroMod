package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.resonance.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.ScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class WaterWalkAbility extends ScheduledPassiveAbility {

    public WaterWalkAbility(@NotNull IAbilityUser user) {
        super(user, "Water Walk", "", 1);
    }

    @Override
    public void execute(boolean isKeyDown) {
        LivingEntity entity = getEntity();
        Vec3d velocity = entity.getVelocity();

        if (isWalkingOnWater()/* && velocity.y < 0*/) {
            /// entity.setPos(entity.getX(), entity.getY() - velocity.y, entity.getZ());

            entity.setOnGround(true);
            entity.setNoGravity(true);
            // entity.setPos(entity.getX(), entity.getBlockY(), entity.getZ());

//            entity.setOnGround(true);
//
//            entity.setVelocity(velocity.x, 0, velocity.z);
//            entity.fallDistance = 0;
//
//            entity.velocityModified = true;
        } else
            entity.setNoGravity(false);
    }

    private boolean isWalkingOnWater() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (entity.isSneaking())
            return false;

        BlockPos feetPos = entity.getBlockPos();
        BlockPos headPos = feetPos.up();
        BlockPos floorPos = feetPos.down();

        BlockState feetState = world.getBlockState(feetPos);
        BlockState headState = world.getBlockState(headPos);
        BlockState floorState = world.getBlockState(floorPos);

        if (feetState.getBlock() instanceof FluidBlock || headState.getBlock() instanceof FluidBlock)
            return false;

        if (!(floorState.getBlock() instanceof FluidBlock floorFluid))
            return false;

        return floorState.getBlock().equals(Blocks.WATER);
    }

}
