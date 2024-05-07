package net.michaeljackson23.mineademia.mixin;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin {


    @Shadow public abstract boolean checkFallFlying();

    //Prints name of player when an item is dropped
    //@Inject( method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    public void dropItem0(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        //Getting the instance of the player from the given class, hints "this"
        PlayerEntity player = (PlayerEntity) (Object) this;

        Mineademia.LOGGER.info(player.getName().getString());
    }

    //Replaces the method altogether
    //@Inject( method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    public void dropItem1(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        //Getting the instance of the player from the given class, hints "this"
        PlayerEntity player = (PlayerEntity) (Object) this;

        //Checks if a value has already been returned
        Mineademia.LOGGER.info( String.valueOf(cir.getReturnValue() != null));

        double d = player.getEyeY() - 0.30000001192092896;
        ItemEntity itemEntity = new ItemEntity(player.getWorld(), player.getX(), d, player.getZ(), stack);
        itemEntity.setPickupDelay(40);
        player.getWorld().removeBlock(new BlockPos(player.getBlockX(), player.getBlockY() - 1, player.getBlockZ()), true);
        cir.setReturnValue(itemEntity);
    }

    //Using Tail allows me to use the temporary variables created in the original method, using capture
    //@Inject( method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void dropItem2(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir, double d, ItemEntity itemEntity) {
        //Getting the instance of the player from the given class, hints "this"
        PlayerEntity player = (PlayerEntity) (Object) this;

        //Checks if a value has already been returned
        Mineademia.LOGGER.info( String.valueOf(cir.getReturnValue() != null));
        itemEntity.setVelocity(0, 2, 0);

        player.getWorld().removeBlock(new BlockPos(player.getBlockX(), player.getBlockY() - 1, player.getBlockZ()), true);
        cir.setReturnValue(cir.getReturnValue());
    }

}
