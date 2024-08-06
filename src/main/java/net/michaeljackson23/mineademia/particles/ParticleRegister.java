package net.michaeljackson23.mineademia.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.particles.Custom.CowlingFactory;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.michaeljackson23.mineademia.particles.Custom.ExplosionFactory;
import net.michaeljackson23.mineademia.particles.Custom.LavaPopFactory;
import net.michaeljackson23.mineademia.particles.Custom.ShockwaveFactory;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegister{

    public static final DefaultParticleType COWLING_PARTICLES = FabricParticleTypes.simple();
    public static final DefaultParticleType EXPLOSION_QUIRK_PARTICLES = FabricParticleTypes.simple();
    public static final DefaultParticleType SHOCKWAVE_PARTICLES = FabricParticleTypes.simple();
    public static final DefaultParticleType LAVA_POP_NO_GRAVITY = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, "cowling_particles"), COWLING_PARTICLES);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, "explosion"), EXPLOSION_QUIRK_PARTICLES);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, "ring"), SHOCKWAVE_PARTICLES);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, "lava_pop_no_gravity"), LAVA_POP_NO_GRAVITY);
    }

    public static void registerClient() {
        ParticleFactoryRegistry.getInstance().register(COWLING_PARTICLES, CowlingFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EXPLOSION_QUIRK_PARTICLES, ExplosionFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SHOCKWAVE_PARTICLES, ShockwaveFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(LAVA_POP_NO_GRAVITY, LavaPopFactory.Factory::new);
    }
}
