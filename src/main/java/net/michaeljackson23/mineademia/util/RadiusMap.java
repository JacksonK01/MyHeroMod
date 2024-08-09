package net.michaeljackson23.mineademia.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RadiusMap {

    private final LinkedHashMap<Float, Float> radiusMap;
    private boolean dirty;

    public RadiusMap() {
        this.radiusMap = new LinkedHashMap<>();
    }
    public RadiusMap(float radius) {
        this();

        put(0, radius);
    }
    public RadiusMap(float radiusStart, float radiusEnd) {
        this();

        put(0, radiusStart);
        put(1, radiusEnd);
    }
    public RadiusMap(@NotNull RadiusMap original) {
        this();

        radiusMap.putAll(original.radiusMap);
        this.dirty = original.dirty;
    }

    public int length() {
        return radiusMap.size();
    }

    public float getRadius(float partial) {
        if (dirty)
            sort();

        partial = Mathf.clamp(0, 1, partial);

        if (length() == 0)
            return 1;

        float previousPartial = -1;

        for (float currentPartial : radiusMap.keySet()) {
            if (previousPartial >= 0 && (previousPartial <= partial && currentPartial >= partial)) {
                float previousRadius = radiusMap.get(previousPartial);
                float currentRadius = radiusMap.get(currentPartial);

                float maxRange = currentPartial - previousPartial;
                float valueInRange = partial - previousPartial;

                float partialRange = valueInRange / maxRange;
                return Mathf.lerp(previousRadius, currentRadius, partialRange);
            }

            previousPartial = currentPartial;
        }

        return radiusMap.get(previousPartial);
    }

    @NotNull
    public RadiusMap put(float partial, float radius) {
        radiusMap.put(Mathf.clamp(0, 1, partial), Math.max(0, radius));

        this.dirty = true;
        return this;
    }

    @NotNull
    public RadiusMap sort() {
        if (!dirty)
            return this;

        Set<Float> keys = radiusMap.keySet();
        LinkedHashSet<Float> sortedKeys = keys.stream().sorted(Float::compare).collect(Collectors.toCollection(LinkedHashSet::new));

        LinkedHashMap<Float, Float> sortedRadiusMap = new LinkedHashMap<>();
        for (float i : sortedKeys)
            sortedRadiusMap.put(i, radiusMap.get(i));

        radiusMap.clear();
        radiusMap.putAll(sortedRadiusMap);

        dirty = false;
        return this;
    }

    @NotNull
    public RadiusMap addConstant(float constantRadius) {
        RadiusMap offsetMap = new RadiusMap(this);
        Set<Float> keys = new HashSet<>(offsetMap.radiusMap.keySet());

        for (float partial : keys)
            offsetMap.put(partial, offsetMap.getRadius(partial) + constantRadius);

        return offsetMap;
    }

    @NotNull
    public RadiusMap addRandom(float minRadius, float maxRadius) {
        return addConstant(Mathf.random(minRadius, maxRadius));
    }

    @NotNull
    public RadiusMap randomizeMap(int pointCount, float minStrength, float maxStrength) {
        pointCount = Math.max(1, pointCount);

        RadiusMap randomizedMap = new RadiusMap(this);
        float[] randomPartials = Mathf.randomArray(0, 1, pointCount);
        for (int i = 0; i < pointCount; i++) {
            float partial = randomPartials[i];
            randomizedMap.put(partial, getRadius(partial) * Mathf.random(minStrength, maxStrength));
        }

        return randomizedMap.sort();
    }

}

