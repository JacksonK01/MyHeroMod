package net.michaeljackson23.mineademia.items.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.networking.Server2Client;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static net.michaeljackson23.mineademia.networking.Server2Client.QUIRK_TABLET_GUI;

@Environment(EnvType.CLIENT)
public class QuirkTablet extends Item {

    public QuirkTablet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            PacketByteBuf data = PacketByteBufs.create();
            ServerPlayNetworking.send((ServerPlayerEntity) player, QUIRK_TABLET_GUI, data);
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
