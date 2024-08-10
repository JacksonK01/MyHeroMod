package net.michaeljackson23.mineademia.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.particles.Custom.CowlingFactory;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.michaeljackson23.mineademia.particles.Custom.ExplosionFactory;
import net.michaeljackson23.mineademia.particles.Custom.QuirkExplosionBeamFactory;
import net.michaeljackson23.mineademia.particles.Custom.ShockwaveFactory;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final DefaultParticleType COWLING_PARTICLES = registerParticle("cowling_particles");
    public static final DefaultParticleType EXPLOSION_QUIRK_PARTICLES = registerParticle("explosion");
    public static final DefaultParticleType SHOCKWAVE_PARTICLES = registerParticle("ring");
    public static final DefaultParticleType QUIRK_EXPLOSION_BEAM = registerParticle("quirk_explosion_beam");

    private static DefaultParticleType registerParticle(String id) {
        DefaultParticleType particleType = FabricParticleTypes.simple();
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, id), particleType);
    }

    public static void register() {
        Mineademia.LOGGER.info("Registering particles for " + Mineademia.MOD_ID);
    }

    public static void registerClient() {
        ParticleFactoryRegistry.getInstance().register(COWLING_PARTICLES, CowlingFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(EXPLOSION_QUIRK_PARTICLES, ExplosionFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SHOCKWAVE_PARTICLES, ShockwaveFactory.Factory::new);
        ParticleFactoryRegistry.getInstance().register(QUIRK_EXPLOSION_BEAM, QuirkExplosionBeamFactory.Factory::new);
    }

}
