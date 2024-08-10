package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class IceSpikeAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {
    final public int MAX_STEEPNESS = 20;
    public static final int MAX_DISTANCE = 30;
    public static final int STAMINA_COST = 50;

    public static  final int MAX_TIME = 20;
    public static  final float STEP_SIZE = 0.5f;
    private final Cooldown cooldown;
    Vec3d position;
    Vec3d startingPosition;
    Vec3d direction;
    double maxDistance;
    int ticks;
    private final HashMap<Long, Integer> affectedBlocks;

    public IceSpikeAbility(@NotNull IAbilityUser user) {
        super(user, "Ice Spike", "Creates a row of spikes", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(10);
        ticks=-1;

        this.affectedBlocks = new HashMap<>();
    }
    public void execute(boolean isKeyDown) {
        if (getStamina() < STAMINA_COST || !isCooldownReadyAndReset())
            return;

        LivingEntity entity = getEntity();

        this.position = entity.getPos();
        this.startingPosition = entity.getPos();
        this.direction = entity.getRotationVecClient();

        if(isKeyDown)
            this.ticks = 0;
        this.affectedBlocks.clear();

        HitResult result = entity.raycast(MAX_DISTANCE, 1, true);
        this.maxDistance = this.position.distanceTo(result.getPos());
    }

    public void onStartTick() {
        if (ticks < 0 || ticks++ > MAX_TIME) {
            ticks = -1;
            return;
        }
        if(ticks > 2) {
            World world = getEntity().getWorld();
            direction = getEntity().getRotationVecClient();

            Vec3d next = this.position.add(this.direction.multiply(STEP_SIZE * ticks));
            BlockPos nextBlock = new BlockPos((int) next.x, (int) next.y, (int) next.z);

            BlockPos floor = getFloorBlock(world, nextBlock);
            if (floor == null)
                return;
            affectedBlocks.put(floor.asLong(), ticks);
            world.setBlockState(floor, BlockRegister.QUIRK_ICE_SPIKE.getDefaultState());

            var normal = direction.normalize();
            Vec3d v1 = new Vec3d(normal.z, 0, -normal.x).normalize();
            Vec3d v2 = v1.crossProduct(normal);

            double width = position.subtract(startingPosition).length();

            v2.multiply(STEP_SIZE * ticks);
            for(var i = direction.multiply(-width); i!= direction.multiply(width); i = i.add(direction.multiply(width).multiply(0.25))){
                Vec3d v3 = v2.multiply(i);
                nextBlock = new BlockPos((int) v3.x, (int) v3.y, (int) v3.z);
                floor = getFloorBlock(world, nextBlock);
                if (floor == null)
                    return;

                getEntity().teleport(nextBlock.getX(), nextBlock.getY(), nextBlock.getZ());
                world.setBlockState(floor, BlockRegister.QUIRK_ICE_SPIKE.getDefaultState());
            }


        }

    }

    @Nullable
    private BlockPos getFloorBlock(@NotNull World world, @NotNull BlockPos next) {
        BlockPos pos = next;
        BlockState belowState = world.getBlockState(pos.down());
        int downCounter = 0;
        int upCounter = 0;

        // going down MAX_STEEPNESS times until the block under is not air
        while (downCounter++ < MAX_STEEPNESS && belowState.isAir()) {
            pos = pos.down();
            belowState = world.getBlockState(pos.down());
        }

        if (downCounter > MAX_STEEPNESS)
            return null;

        while (upCounter++ < MAX_STEEPNESS && !world.getBlockState(pos).isAir()) {
            pos = pos.up();
        }

        if (upCounter > MAX_STEEPNESS)
            return null;

        return pos;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }
    private int getMaxGrowth(Vec3d playerPosition, Vec3d currentPostion){
        return Mathf.clamp( 0, 5, (int)(Math.abs(playerPosition.x-currentPostion.x)/2 + Math.abs((playerPosition.z-currentPostion.z)))/2);
    }

   /* @Override
    public void execute(boolean isKeyDown) {
        RaycastToEntity.raycast(getEntity(), 10, this);
    }

    @Override
    public void action(ServerPlayerEntity player, double x, double y, double z) {
        BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
        World world = player.getWorld();
        BlockState state = world.getBlockState(pos);
        while(world.getBlockState(pos.down()).isAir()){
            pos = pos.down();
        }
        while(!world.getBlockState(pos).isAir() ){
            pos = pos.up();
        }
        if(Math.abs(player.getX()-x) > 1 || (player.getZ()-z) > 1)
            world.setBlockState(pos, BlockRegister.QUIRK_ICE_SPIKE.getDefaultState().with(QuirkIceSpikeBlock.MAX_GROWTH, Mathf.clamp( 0, 5, (int)(Math.abs(player.getX()-x)/2 + Math.abs((player.getZ()-z)))/2)));
    }
    @Nullable
    private BlockPos getFloorBlock(@NotNull World world, @NotNull BlockPos next) {
        BlockPos pos = next;
        BlockState belowState = world.getBlockState(pos.down());
        int downCounter = 0;
        int upCounter = 0;

        // going down MAX_STEEPNESS times until the block under is not air
        while (downCounter++ < MAX_STEEPNESS && belowState.isAir()) {
            pos = pos.down();
            BlockState state = world.getBlockState(pos.down());
        }

        if (downCounter > MAX_STEEPNESS)
            return null;

        while (upCounter++ < MAX_STEEPNESS && !world.getBlockState(pos).isAir()) {

        }

    }*/

}
