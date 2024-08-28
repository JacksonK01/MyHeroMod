package net.michaeljackson23.mineademia.entity.projectile.rifle;

import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RifleAmmoEntity extends ArrowEntity {

    public RifleAmmoEntity(@NotNull World world, @NotNull Vec3d spawnPosition) {
        super(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, getProjectileItem());
    }

    @NotNull
    private static ItemStack getProjectileItem() {
        return new ItemStack(Items.ARROW, 1);
    }

}
