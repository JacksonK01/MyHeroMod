package net.michaeljackson23.mineademia.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.entity.cube.CubeEntity;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegister {
    public static final EntityType<CubeEntity> CUBE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Mineademia.Mod_id, "cube"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CubeEntity::new).dimensions(
                    EntityDimensions.fixed(0.75f, 0.75f)).build());

    public static final EntityType<AirForceProjectile> AIR_FORCE_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Mineademia.Mod_id, "air_force"),
            FabricEntityTypeBuilder.<AirForceProjectile>create(SpawnGroup.MISC, AirForceProjectile::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());

    }
}
