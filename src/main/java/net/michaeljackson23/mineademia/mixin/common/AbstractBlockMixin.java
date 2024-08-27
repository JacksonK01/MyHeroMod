package net.michaeljackson23.mineademia.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.NoClipAbility;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockMixin {

    @ModifyReturnValue(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"))
    private VoxelShape getCollisionShape(VoxelShape original, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        Entity entity;
        if (original.isEmpty() || !(shapeContext instanceof EntityShapeContext esc) || (entity = esc.getEntity()) == null) {
            return original;
        }

        if (!(entity instanceof LivingEntity livingEntity))
            return original;

        IAbilityUser user = AbilityManager.getUser(livingEntity);
        if (user == null)
            return original;

        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
        if (noClipAbility == null || !noClipAbility.isClipping())
            return original;

        return VoxelShapes.empty();
    }

    @WrapWithCondition(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V"))
    private boolean onEntityCollision(Block instance, BlockState state, World world, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return false;

        IAbilityUser user = AbilityManager.getUser(livingEntity);
        if (user == null)
            return false;

        NoClipAbility noClipAbility = user.getAbility(NoClipAbility.class);
        return noClipAbility != null && noClipAbility.isClipping();
    }


}
