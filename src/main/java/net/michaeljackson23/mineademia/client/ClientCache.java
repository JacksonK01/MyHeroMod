package net.michaeljackson23.mineademia.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityset.AbilityMap;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityUserPacketS2C;
import net.michaeljackson23.mineademia.datastructures.typesafemap.IReadonlyTypesafeMap;
import net.michaeljackson23.mineademia.util.Mathf;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ClientCache {

    public static final float ZOOM_SPEED = 0.1f;


    public static AbilityUserPacketS2C self;
    public static final HashMap<UUID, AbilityUserPacketS2C> users = new HashMap<>();

    private static final HashMap<UUID, Float> desiredZoomLevel = new HashMap<>();
    private static final HashMap<UUID, Float> actualZoomLevel = new HashMap<>();

    public static <T extends IAbility> @NotNull HashSet<IReadonlyTypesafeMap> getAbilities(@NotNull Class<T> type, boolean exact) {
        HashSet<IReadonlyTypesafeMap> result = new HashSet<>();

        users.forEach((uuid, user) -> {
            user.getAbilityMap().forEach((abilityType, map) -> {
                if (AbilityMap.isMatchingRequirements(abilityType, type, exact))
                    result.add(map);
            });
        });

        return result;
    }
    public static <T extends IAbility> @NotNull HashSet<IReadonlyTypesafeMap> getAbilities(@NotNull Class<T> type) {
        return getAbilities(type, false);
    }

    public static <T extends IAbility> @NotNull HashSet<IReadonlyTypesafeMap> getSelfAbilities(@NotNull Class<T> type, boolean exact) {
        HashSet<IReadonlyTypesafeMap> result = new HashSet<>();

        self.getAbilityMap().forEach((abilityType, map) -> {
            if (AbilityMap.isMatchingRequirements(abilityType, type, exact))
                result.add(map);
        });

        return result;
    }
    public static <T extends IAbility> @NotNull HashSet<IReadonlyTypesafeMap> getSelfAbilities(@NotNull Class<T> type) {
        return getSelfAbilities(type, false);
    }

    public static void setZoomLevel(@NotNull UUID uuid, float zoomLevel) {
        desiredZoomLevel.put(uuid, zoomLevel);
    }

    public static float getZoomLevel(@NotNull UUID uuid) {
        if (!desiredZoomLevel.containsKey(uuid))
            return 1;

        float desired = desiredZoomLevel.get(uuid);
        float actual = actualZoomLevel.getOrDefault(uuid, 1f);

        float nextActual = Mathf.lerp(actual, desired, ZOOM_SPEED);
        actualZoomLevel.put(uuid, nextActual);

        return nextActual;
    }

}
