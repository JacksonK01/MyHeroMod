package net.michaeljackson23.mineademia.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

public class AreaOfEffect {
    public static void execute(ServerPlayerEntity owner, int radius, ActionOnEntity actionOnEntity, int x, int y, int z) {
        Box radiusBox = new Box(x - radius, y, z - radius, x + radius, y, z + radius);
    }


    public interface ActionOnEntity {
        void action();
    }
}
