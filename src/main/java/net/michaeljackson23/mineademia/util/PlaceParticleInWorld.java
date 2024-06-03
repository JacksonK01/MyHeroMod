package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class PlaceParticleInWorld {
    //This is creating a similar effect to the command
    public static void spawn(World world, ParticleEffect type, double x, double y, double z, double dx, double dy, double dz, int amount) {
        for (int counter = 0; counter < amount; counter++) {

            Random random = new Random();
            double offsetX = random.nextGaussian() * dx;
            double offsetY = random.nextGaussian() * dy;
            double offsetZ = random.nextGaussian() * dz;

            world.addImportantParticle(
                    type, true,
                    x + offsetX,
                    y + offsetY,
                    z + offsetZ,
                    0, 0, 0
            );
        }
    }
}
