package net.michaeljackson23.mineademia.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class AffectAll<T extends Entity> {

    private final HashSet<T> entities;

    private AffectAll(List<T> entities) {
        this.entities = new HashSet<>(entities);
    }

    public AffectAll<T> exclude(T entity) {
        entities.remove(entity);
        return this;
    }

    public AffectAll<T> with(Consumer<T> action) {
        entities.forEach(action);
        return this;
    }

    public AffectAll<T> withVelocity(Vec3d velocity, boolean set) {
        entities.forEach((e) -> affectWithVelocity(e, velocity, set));
        return this;
    }


    // BOX

    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, @NotNull Box box, @NotNull Predicate<Entity> filter) {
        return new AffectAll<>(world.getOtherEntities(null, box, filter));
    }
    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, @NotNull Box box) {
        return withinBox(world, box, (e) -> true);
    }

    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, @NotNull Vec3d from, @NotNull Vec3d to, @NotNull Predicate<Entity> filter) {
        return withinBox(world, new Box(from, to), filter);
    }
    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, @NotNull Vec3d from, @NotNull Vec3d to) {
        return withinBox(world, from, to, (e) -> true);
    }

    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, float xs, float ys, float zs, float xe, float ye, float ze, @NotNull Predicate<Entity> filter) {
        return withinBox(world, new Box(xs, ys, zs, xe, ye, ze), filter);
    }
    @NotNull
    public static AffectAll<?> withinBox(@NotNull World world, float xs, float ys, float zs, float xe, float ye, float ze) {
        return withinBox(world, xs, ys, zs, xe, ye, ze, (e) -> true);
    }

    // GENERIC

    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, @NotNull Box box, @NotNull Predicate<T> filter) {
        return new AffectAll<>(world.getEntitiesByClass(type, box, filter));
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, @NotNull Box box) {
        return withinBox(type, world, box, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d from, @NotNull Vec3d to, @NotNull Predicate<T> filter) {
        return withinBox(type, world, new Box(from, to), filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d from, @NotNull Vec3d to) {
        return withinBox(type, world, from, to, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, float xs, float ys, float zs, float xe, float ye, float ze, @NotNull Predicate<T> filter) {
        return withinBox(type, world, new Box(xs, ys, zs, xe, ye, ze), filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinBox(@NotNull Class<T> type, @NotNull World world, float xs, float ys, float zs, float xe, float ye, float ze) {
        return withinBox(type, world, xs, ys, zs, xe, ye, ze, (e) -> true);
    }

    // RADIUS

    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, Vec3d radius, @NotNull Predicate<Entity> filter) {
        Box box = new Box(center.subtract(radius), center.add(radius));
        return withinBox(world, box, filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, Vec3d radius) {
        return withinRadius(world, center, radius, (e) -> true);
    }

    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, Vec3d radius, @NotNull Predicate<Entity> filter) {
        return withinRadius(world, new Vec3d(x, y, z), radius, filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, Vec3d radius) {
        return withinRadius(world, x ,y, z, radius, (e) -> true);
    }


    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, float radiusX, float radiusY, float radiusZ, @NotNull Predicate<Entity> filter) {
        return withinRadius(world, center, new Vec3d(radiusX, radiusY, radiusZ), filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, float radiusX, float radiusY, float radiusZ) {
        return withinRadius(world, center, radiusX, radiusY, radiusZ, (e) -> true);
    }

    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, float radiusX, float radiusY, float radiusZ, @NotNull Predicate<Entity> filter) {
        return withinRadius(world, new Vec3d(x, y, z), radiusX, radiusY, radiusZ, filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, float radiusX, float radiusY, float radiusZ) {
        return withinRadius(world, x ,y, z, radiusX, radiusY, radiusZ, (e) -> true);
    }


    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, float radius, @NotNull Predicate<Entity> filter) {
        return withinRadius(world, center, radius, radius, radius, filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, @NotNull Vec3d center, float radius) {
        return withinRadius(world, center, radius, (e) -> true);
    }

    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, float radius, @NotNull Predicate<Entity> filter) {
        return withinRadius(world, new Vec3d(x, y, z), radius, filter);
    }
    @NotNull
    public static AffectAll<?> withinRadius(@NotNull World world, float x, float y, float z, float radius) {
        return withinRadius(world, x ,y, z, radius, (e) -> true);
    }

    // GENERIC

    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, Vec3d radius, @NotNull Predicate<T> filter) {
        Box box = new Box(center.subtract(radius), center.add(radius));
        return withinBox(type, world, box, filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, Vec3d radius) {
        return withinRadius(type, world, center, radius, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, Vec3d radius, @NotNull Predicate<T> filter) {
        return withinRadius(type, world, new Vec3d(x, y, z), radius, filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, Vec3d radius) {
        return withinRadius(type, world, x ,y, z, radius, (e) -> true);
    }


    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, float radiusX, float radiusY, float radiusZ, @NotNull Predicate<T> filter) {
        return withinRadius(type, world, center, new Vec3d(radiusX, radiusY, radiusZ), filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, float radiusX, float radiusY, float radiusZ) {
        return withinRadius(type, world, center, radiusX, radiusY, radiusZ, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, float radiusX, float radiusY, float radiusZ, @NotNull Predicate<T> filter) {
        return withinRadius(type, world, new Vec3d(x, y, z), radiusX, radiusY, radiusZ, filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, float radiusX, float radiusY, float radiusZ) {
        return withinRadius(type, world, x ,y, z, radiusX, radiusY, radiusZ, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, float radius, @NotNull Predicate<T> filter) {
        Box box = new Box(center.subtract(radius, radius, radius), center.add(radius, radius, radius));
        return withinBox(type, world, box, filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, @NotNull Vec3d center, float radius) {
        return withinRadius(type, world, center, radius, (e) -> true);
    }

    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, float radius, @NotNull Predicate<T> filter) {
        return withinRadius(type, world, new Vec3d(x, y, z), radius, filter);
    }
    @NotNull
    public static <T extends Entity> AffectAll<T> withinRadius(@NotNull Class<T> type, @NotNull World world, float x, float y, float z, float radius) {
        return withinRadius(type, world, x ,y, z, radius, (e) -> true);
    }



    private static <T extends Entity> void affectWithVelocity(T entity, Vec3d velocity, boolean set) {
        if (set)
            entity.setVelocity(velocity);
        else
            entity.addVelocity(velocity);
    }

}
