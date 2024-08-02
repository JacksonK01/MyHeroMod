package net.michaeljackson23.mineademia.util;

public final class Mathf {

    private Mathf() { }


    public static float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(max, value));
    }
    public static int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }

    public static float lerp(float min, float max, float alpha) {
        return min + alpha * (max - min);
    }

}
