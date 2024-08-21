package net.michaeljackson23.mineademia.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class Mathf {

    private Mathf() { }

    private static final Random r = new Random();

    public static final double EPSILON = 1e-15F;

    public static final double Rad2Deg = 360 / (Math.PI * 2);


    public static boolean isZero(float value) {
        return value >= -EPSILON && value <= EPSILON;
    }
    public static boolean isZero(double value) {
        return value >= -EPSILON && value <= EPSILON;
    }

    public static float clamp(float min, float max, float value) {
        if (min > max)
            return clamp(max, min, value);

        return Math.max(min, Math.min(max, value));
    }
    public static int clamp(int min, int max, int value) {
        if (min > max)
            return clamp(max, min, value);

        return Math.max(min, Math.min(max, value));
    }

    public static float lerp(float min, float max, float alpha) {
        if (min > max)
            return lerp(max, min, 1 - alpha);

        return min + alpha * (max - min);
    }

    public static float random(float min, float max) {
        if (min > max)
            return random(max, min);

        return r.nextFloat() * (max - min) + min;
    }

    public static float @NotNull [] randomArray(float min, float max, int amount) {
        float[] result = new float[amount];

        for (int i = 0; i < amount; i++)
            result[i] = random(min, max);

        return result;
    }


    @NotNull
    public static BlockPos getBlock(@NotNull Vec3d pos) {
        return new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
    }


    public static class Vector {

        public static final Vec3d ONE = new Vec3d(1, 1, 1);
        public static final Vec3d UP = new Vec3d(0, 1, 0);
        public static final Vec3d POS_Z = new Vec3d(0, 0, 1);


        @NotNull
        public static Vec3d random() {
            return new Vec3d(Mathf.random(-1, 1), Mathf.random(-1, 1), Mathf.random(-1, 1));
        }

        @NotNull
        public static Vec3d getOrthogonal(@NotNull Vec3d normal) {
            if (normal == Vec3d.ZERO)
                return Vec3d.ZERO;
            else if (isZero(normal.x) && isZero(normal.z))
                return POS_Z;
            else
                return new Vec3d(normal.z, 0, -normal.x).normalize();
        }

        public static float angleBetweenVectors(@NotNull Vec3d from, @NotNull Vec3d to)
        {
            // sqrt(a) * sqrt(b) = sqrt(a * b) -- valid for real numbers
            float denominator = (float)Math.sqrt(from.length() * to.length());
            if (denominator < EPSILON)
                return 0F;

            float dot = Mathf.clamp((float) from.dotProduct(to) / denominator, -1F, 1F);
            return (float) (Math.acos(dot) * Rad2Deg);
        }

        @NotNull
        public static Vec3d lerp(@NotNull Vec3d from, @NotNull Vec3d to, float alpha) {
            return new Vec3d(Mathf.lerp((float) from.x, (float) to.x, alpha), Mathf.lerp((float) from.y, (float) to.y, alpha), Mathf.lerp((float) from.z, (float) to.z, alpha));
        }

    }

}
