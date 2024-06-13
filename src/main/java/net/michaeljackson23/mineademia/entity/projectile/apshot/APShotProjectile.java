package net.michaeljackson23.mineademia.entity.projectile.apshot;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.util.PlaceParticleInWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class APShotProjectile extends ThrownItemEntity {
    int timer = 0;

    public APShotProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public APShotProjectile(World world, LivingEntity owner) {
        super(null, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        if(timer >= 50) {
            kill();
            return;
        }
        timer++;
        PlaceParticleInWorld.spawn(getWorld(), ParticleRegister.EXPLOSION_QUIRK_PARTICLES, getX(), getY(), getZ(), 0.3, 0.3, 0.3, 3);
    }
}
