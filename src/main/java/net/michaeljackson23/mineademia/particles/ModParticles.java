package net.michaeljackson23.mineademia.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.particles.Custom.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final DefaultParticleType COWLING_PARTICLES = registerParticle("cowling_particles");
    public static final DefaultParticleType ELECTRIFICATION_PARTICLES = registerParticle("electrification_particles");
    public static final DefaultParticleType SHOCKWAVE_PARTICLES = registerParticle("ring");
    public static final DefaultParticleType QUIRK_OFA_CLOUD = registerParticle("quirk_ofa_cloud");

    public static final DefaultParticleType QUIRK_EXPLOSION_SHORT = registerParticle("quirk_explosion_short");
    public static final DefaultParticleType QUIRK_EXPLOSION_LONG = registerParticle("quirk_explosion_long");
    public static final DefaultParticleType QUIRK_EXPLOSION_BEAM = registerParticle("quirk_explosion_beam");

    private static DefaultParticleType registerParticle(String id) {
        DefaultParticleType particleType = FabricParticleTypes.simple();
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Mineademia.MOD_ID, id), particleType);
    }

    public static void registerServer() {
        Mineademia.LOGGER.info("Registering particles for " + Mineademia.MOD_ID);
    }

    public static void registerClient() {
        ParticleFactoryRegistry instance = ParticleFactoryRegistry.getInstance();

        instance.register(COWLING_PARTICLES, CowlingFactory.Factory::new);
        instance.register(SHOCKWAVE_PARTICLES, ShockwaveFactory.Factory::new); // has color, not done yet

        instance.register(QUIRK_EXPLOSION_SHORT, ModParticle.createFactory(0.45f, 6));
        instance.register(QUIRK_EXPLOSION_LONG, ModParticle.createFactory(0.45f, 24));
        instance.register(QUIRK_EXPLOSION_BEAM, ModParticle.createFactory(0.45f, 4));

        instance.register(ELECTRIFICATION_PARTICLES, ElectrificationFactory.Factory::new);

        instance.register(QUIRK_OFA_CLOUD, ModParticle.createFactory(0.1f, 1));
    }

}
