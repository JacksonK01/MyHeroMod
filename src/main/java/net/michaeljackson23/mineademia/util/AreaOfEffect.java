package net.michaeljackson23.mineademia.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class AreaOfEffect {

    //THIS IS CURRENTLY BROKEN DO NOT USE
    private final static boolean DRAW_HITBOX = false;

    public static void execute(LivingEntity owner, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(
                x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = owner.getWorld().getOtherEntities(owner, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if (entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));

        if(DRAW_HITBOX && owner instanceof ServerPlayerEntity player) {
            sendDrawHitBoxPackage(player, radiusBox);
        }
    }



    public static void execute(World world, double radiusInBlocks, double yRadiusInBlocks, double x, double y, double z, ActionOnEntity actionOnEntity) {
        Box radiusBox = new Box(
                x - radiusInBlocks, y - yRadiusInBlocks, z - radiusInBlocks,
                x + radiusInBlocks, y + yRadiusInBlocks, z + radiusInBlocks);

        List<Entity> entitiesInRadius = world.getOtherEntities(null, radiusBox);
        entitiesInRadius.forEach((entityToAffect -> {
            if (entityToAffect instanceof LivingEntity) {
                actionOnEntity.action((LivingEntity) entityToAffect);
            }
        }));
    }

    private static void sendDrawHitBoxPackage(ServerPlayerEntity player, Box box) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(box.minX);
        buf.writeDouble(box.minY);
        buf.writeDouble(box.minZ);
        buf.writeDouble(box.maxX);
        buf.writeDouble(box.maxY);
        buf.writeDouble(box.maxZ);
        ServerPlayNetworking.send(player, Networking.S2C_DRAW_BOX, buf);
    }

    @FunctionalInterface
    public interface ActionOnEntity {
        void action(LivingEntity entityToAffect);
    }

}
