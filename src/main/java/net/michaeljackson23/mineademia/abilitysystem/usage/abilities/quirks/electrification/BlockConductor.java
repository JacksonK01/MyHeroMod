package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.electrification;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.PassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

public class BlockConductor extends PassiveAbility implements ITickAbility {

    private static final ArrayList<BlockSoundGroup> METAL_SOUNDS = new ArrayList<>();
    private static final int MAX_DISTANCE = 10;

    LinkedList<BlockPos> blocksCache = new LinkedList<>();

    static {
        METAL_SOUNDS.add(BlockSoundGroup.METAL);
        METAL_SOUNDS.add(BlockSoundGroup.NETHERITE);
        METAL_SOUNDS.add(BlockSoundGroup.ANVIL);
        METAL_SOUNDS.add(BlockSoundGroup.COPPER);
        METAL_SOUNDS.add(BlockSoundGroup.COPPER_GRATE);
        METAL_SOUNDS.add(BlockSoundGroup.COPPER_BULB);
        METAL_SOUNDS.add(BlockSoundGroup.LANTERN);
        METAL_SOUNDS.add(BlockSoundGroup.ANCIENT_DEBRIS);
        METAL_SOUNDS.add(BlockSoundGroup.CHAIN);
    }

    public BlockConductor(@NotNull IAbilityUser user) {
        super(user, "Block Conductor", "Every metal block will extend an electric current.");
    }

    @Override
    public void execute(boolean isKeyDown) {

    }

    @Override
    public void onStartTick() {
        getMetalBlocksInArea();
    }

    private void getMetalBlocksInArea() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = getServerWorld();

        BlockPos blockPos = entity.getBlockPos().down();

        blocksCache.clear();
        activateMetalBlocksInArea(blockPos);
    }

    private void activateMetalBlocksInArea(BlockPos pos) {
        activateMetalBlocksInArea(pos, pos);
    }

    private void activateMetalBlocksInArea(BlockPos newPos, BlockPos originPos) {
        ServerWorld serverWorld = getServerWorld();
        LivingEntity livingEntity = getEntity();

        if(!isMetalBlock(newPos, serverWorld) || !isWithinRange(newPos, originPos) || blocksCache.contains(newPos)) {
            return;
        }

        blocksCache.add(newPos);
        DrawParticles.forWorld(serverWorld).spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                newPos.toCenterPos(), 1, 0.5f, 0.5f, 0.5f, 0, true);
        AffectAll.withinRadius(LivingEntity.class, serverWorld, newPos.toCenterPos(), 1, 1, 1).exclude(livingEntity).with(entityToAffect -> {
            entityToAffect.setVelocity(0, 0, 0);
            entityToAffect.velocityModified = true;
        });

        activateMetalBlocksInArea(newPos.north(), originPos);
        activateMetalBlocksInArea(newPos.east(), originPos);
        activateMetalBlocksInArea(newPos.south(), originPos);
        activateMetalBlocksInArea(newPos.west(), originPos);
    }

    private boolean isMetalBlock(BlockPos pos, ServerWorld serverWorld) {
        return METAL_SOUNDS.contains(serverWorld.getBlockState(pos).getSoundGroup());
    }

    private boolean isWithinRange(BlockPos p1, BlockPos p2) {
        return p1.getSquaredDistance(p2) <= MAX_DISTANCE;
    }

}
