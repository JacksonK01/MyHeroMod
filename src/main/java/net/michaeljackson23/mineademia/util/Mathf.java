package net.michaeljackson23.mineademia.util;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class Mathf {

    private Mathf() { }

    private static final Random r = new Random();


    public static float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }
    public static int clamp(int min, int max, int value) {
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

    public static float[] randomArray(float min, float max, int amount) {
        float[] result = new float[amount];

        for (int i = 0; i < amount; i++)
            result[i] = random(min, max);

        return result;
    }

}
