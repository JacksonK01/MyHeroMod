package net.michaeljackson23.mineademia.entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.entity.cube.CubeEntity;
import net.michaeljackson23.mineademia.entity.cube.CubeEntityModel;
import net.michaeljackson23.mineademia.entity.cube.CubeEntityRenderer;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.entity.projectile.windblade.WindBladeProjectile;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegister {
    public static final EntityType<CubeEntity> CUBE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Mineademia.MOD_ID, "cube"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CubeEntity::new).dimensions(
                    EntityDimensions.fixed(0.75f, 0.75f)).build());

    public static final EntityType<AirForceProjectile> AIR_FORCE_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Mineademia.MOD_ID, "air_force"),
            FabricEntityTypeBuilder.<AirForceProjectile>create(SpawnGroup.MISC, AirForceProjectile::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static final EntityType<WindBladeProjectile> WIND_BLADE_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Mineademia.MOD_ID, "wind_blade"),
            FabricEntityTypeBuilder.<WindBladeProjectile>create(SpawnGroup.MISC, WindBladeProjectile::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(CUBE, CubeEntity.createMobAttributes());

    }

    public static void registerModels() {
        EntityRendererRegistry.register(EntityRegister.CUBE, CubeEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CubeEntityRenderer.MODEL_CUBE_LAYER, CubeEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(EntityRegister.AIR_FORCE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.WIND_BLADE_PROJECTILE, FlyingItemEntityRenderer::new);
    }
}
