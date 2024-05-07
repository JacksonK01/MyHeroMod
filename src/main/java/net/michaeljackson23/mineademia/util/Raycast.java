package net.michaeljackson23.mineademia.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class Raycast {
    public static HitResult start(ServerPlayerEntity player, World world, int distance) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = start.add(player.getRotationVec(1.0f).multiply(distance));



        Box box = new Box(start, end);


        HitResult result = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player));
//        HitResult result = player.raycast(10, 1.0f, false);

        return result;
    }
}
