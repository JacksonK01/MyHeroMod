package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

public class NoClipAbility extends DataPassiveAbility {

    public static final HashSet<Block> DEFAULT_BLACKLISTED_BLOCKS = new HashSet<>() {{
        add(Blocks.BEDROCK);
        add(Blocks.END_PORTAL_FRAME);

        add(Blocks.WATER);
        add(Blocks.LAVA);

        add(Blocks.NETHER_PORTAL);
        add(Blocks.END_PORTAL);
    }};


    private boolean noClipping;
    private boolean fallOnSneak;

    private int maxVisionRadius;

    private final HashSet<Block> blacklistedBlocks;

    private Predicate<Entity> entityCondition;
    private Predicate<BlockPos> noClipCondition;

    public NoClipAbility(@NotNull IAbilityUser user) {
        super(user, "No Clip", "Is No Clipping");

        this.noClipping = false;
        this.fallOnSneak = true;

        this.maxVisionRadius = 0;

        this.blacklistedBlocks = new HashSet<>();
        blacklistedBlocks.addAll(DEFAULT_BLACKLISTED_BLOCKS);

        this.entityCondition = (e) -> e.equals(getEntity());
    }

    public boolean isNoClipping() {
        return noClipping;
    }
    public void setNoClipping(boolean clipping) {
        this.noClipping = clipping;
    }

    public boolean isFallOnSneak() {
        return fallOnSneak;
    }
    public void setFallOnSneak(boolean fallOnSneak) {
        this.fallOnSneak = fallOnSneak;
    }

    public int getMaxVisionRadius() {
        return maxVisionRadius;
    }
    public void setMaxVisionRadius(int maxVisionRadius) {
        this.maxVisionRadius = Math.max(0, this.maxVisionRadius);
    }

    public boolean canNoClipThrough(@NotNull Entity entity, @NotNull BlockPos pos) {
        Block block = getEntity().getWorld().getBlockState(pos).getBlock();
        return isNoClipping() && !blacklistedBlocks.contains(block) && (noClipCondition == null || noClipCondition.test(pos)) && entityCondition.test(entity);
    }

    public void setNoClipThrough(@NotNull Block... blocks) {
        blacklistedBlocks.addAll(List.of(blocks));
    }
    public void removeNoClipThrough(@NotNull Block... blocks) {
        List.of(blocks).forEach(blacklistedBlocks::remove);
    }

    public boolean shouldNoClipDown(@NotNull Entity entity) {
        return entityCondition.test(entity) && (!fallOnSneak || entity.isSneaking());
    }

    public void setNoClipCondition(@NotNull Predicate<BlockPos> noClipCondition) {
        this.noClipCondition = noClipCondition;
    }
    public void setEntityCondition(@NotNull Predicate<Entity> entityCondition) {
        this.entityCondition = entityCondition;
    }

    public boolean testEntityCondition(@NotNull Entity entity) {
        return entityCondition.test(entity);
    }

    public boolean isSubmerged(boolean feetOnly) {
        LivingEntity entity = getEntity();
        World world = entity.getWorld();

        boolean feetSubmerged = !world.getBlockState(entity.getBlockPos()).isAir();
        if (feetOnly)
            return feetSubmerged;

        boolean headSubmerged = !world.getBlockState(entity.getBlockPos().up()).isAir();
        return feetSubmerged && headSubmerged;
    }

    public static boolean isNoClipping(@NotNull Entity entity) {
        NoClipAbility noClipAbility = IAbility.getAbility(entity, NoClipAbility.class);
        return noClipAbility != null && noClipAbility.isNoClipping();
    }

}
