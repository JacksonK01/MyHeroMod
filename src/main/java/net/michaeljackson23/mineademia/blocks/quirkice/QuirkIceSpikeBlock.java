package net.michaeljackson23.mineademia.blocks.quirkice;

import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.statuseffects.QuirkEffectUtil;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.block.*;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;


public class QuirkIceSpikeBlock extends Block {
    public static final IntProperty MAX_GROWTH = IntProperty.of("max_growth", 0, 5);
    public static final IntProperty AGE = Properties.AGE_15;
    public static final EnumProperty<Thickness> THICKNESS = Properties.THICKNESS;
    private static final VoxelShape TIP_MERGE_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape UP_TIP_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private static final VoxelShape FRUSTUM_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);


    public QuirkIceSpikeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(MAX_GROWTH, 4).with(THICKNESS, Thickness.TIP).with(AGE, 0));
    }
    private static void tryGrow(ServerWorld world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        if (isTip(blockState, direction.getOpposite())) {
            growMerged(blockState, world, blockPos);
        } else if (blockState.isAir() || blockState.isOf(Blocks.WATER)) {
            place(world, blockPos, world.getBlockState(pos).get(AGE), direction, Thickness.TIP, world.getBlockState(pos).get(MAX_GROWTH));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(MAX_GROWTH).add(THICKNESS).add(AGE));
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Thickness thickness = state.get(THICKNESS);
        VoxelShape voxelShape = thickness == Thickness.TIP_MERGE ? TIP_MERGE_SHAPE : (thickness == Thickness.TIP ? UP_TIP_SHAPE : (thickness == Thickness.FRUSTUM ? BASE_SHAPE : (thickness == Thickness.MIDDLE ? FRUSTUM_SHAPE : MIDDLE_SHAPE)));
        Vec3d vec3d = state.getModelOffset(world, pos);
        return voxelShape.offset(vec3d.x, 0.0, vec3d.z);
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(world instanceof ServerWorld serverWorld)
            scheduledTick(state, serverWorld, pos, world.random);
    }

    private static boolean isTip(BlockState state, boolean allowMerged) {
        if (!state.isOf(BlockRegister.QUIRK_ICE)) {
            return false;
        }
        Thickness thickness = state.get(THICKNESS);
        return thickness == Thickness.TIP || allowMerged && thickness == Thickness.TIP_MERGE;
    }
    private static boolean isTip(BlockState state, Direction direction) {
        return isTip(state, false) && direction == Direction.UP;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        }
        if (!this.canPlaceAt(state, world, pos)) {
            return state;
        }
        Thickness thickness = getThickness(world, pos);
        return state.with(THICKNESS, thickness);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 5);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }
    private static Thickness getThickness(WorldView world, BlockPos pos) {
        BlockState blockStateUp = world.getBlockState(pos.offset(Direction.UP));
        BlockState blockStateDown = world.getBlockState(pos.offset(Direction.DOWN));
        if (!blockStateUp.isOf(BlockRegister.QUIRK_ICE_SPIKE)) {
            return Thickness.TIP;
        }
        Thickness thickness = blockStateUp.get(THICKNESS);
        if (thickness == Thickness.TIP) {
            return Thickness.FRUSTUM;
        }

        if (!blockStateDown.isOf(BlockRegister.QUIRK_ICE_SPIKE)) {
            return Thickness.BASE;
        }
        return Thickness.MIDDLE;
    }
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        for(Entity entity : world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(0, 0,0), entity -> true)){
            if(entity instanceof  LivingEntity livingEntity){
                livingEntity.damage(new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.FREEZE)),2f);
                QuirkEffectUtil.applyFrozen(livingEntity, 30);
            }
        }
        if(state.get(AGE)<15) {
            world.setBlockState(pos, state.with(AGE, state.get(AGE)+1));
            world.scheduleBlockTick(pos, this, 10);
        }else
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        if(state.get(AGE)<state.get(MAX_GROWTH))
            tryGrow(world, pos, Direction.UP);


    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    }
    private static void growMerged(BlockState state, WorldAccess world, BlockPos pos) {
        BlockPos blockPos2;
        BlockPos blockPos;
        blockPos = pos;
        blockPos2 = pos.up();
        place(world, blockPos2, state.get(AGE), Direction.DOWN, Thickness.TIP_MERGE, state.get(MAX_GROWTH));
        place(world, blockPos, state.get(AGE), Direction.UP, Thickness.TIP_MERGE, state.get(MAX_GROWTH));
    }
    private static void place(WorldAccess world, BlockPos pos, int age, Direction direction, Thickness thickness, int max_growth) {
        BlockState blockState = BlockRegister.QUIRK_ICE_SPIKE.getDefaultState().with(THICKNESS, thickness).with(AGE, age).with(MAX_GROWTH, max_growth);
        world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
    }
}
