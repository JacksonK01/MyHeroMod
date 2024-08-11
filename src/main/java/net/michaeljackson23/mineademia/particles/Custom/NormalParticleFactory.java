package net.michaeljackson23.mineademia.particles.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NormalParticleFactory extends SpriteBillboardParticle  {

    private final SpriteProvider spriteProvider;

    protected NormalParticleFactory(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, float upwardsAcceleration, float scale, int maxAge) {
        super(world, x, y, z);
        this.velocityMultiplier = 1f;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.maxAge = maxAge;
        this.gravityStrength = upwardsAcceleration;
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }


    @Environment(EnvType.CLIENT)
    public static class FactoryResult implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider sprites;

        private final float scale;
        private final int maxAge;

        public FactoryResult(SpriteProvider spriteSet, float scale, int maxAge) {
            this.sprites = spriteSet;

            this.scale = scale;
            this.maxAge = maxAge;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new NormalParticleFactory(world, x, y, z, this.sprites, 0f, scale, maxAge);
        }

    }

    @Environment(EnvType.CLIENT)
    public static class Factory {

        @NotNull
        public static <T extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<T> createFactory(float scale, int maxAge) {
            return provider -> (ParticleFactory<T>) new FactoryResult(provider, scale, maxAge);
        }

    }

}
