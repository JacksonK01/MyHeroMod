package net.michaeljackson23.mineademia.items.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MockQuirkTablet extends Item {

    public MockQuirkTablet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, Networking.MOCK_QUIRK_TABLET_GUI, PacketByteBufs.empty());
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
