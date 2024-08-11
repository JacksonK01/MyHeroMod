package net.michaeljackson23.mineademia.particles.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class QuirkResonanceFactory extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    protected QuirkResonanceFactory(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, float upwardsAcceleration) {
        super(world, x, y, z);
        this.velocityMultiplier = 1f;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = 0.8f;
        this.maxAge = 1;
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
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new QuirkExplosionBeamFactory(world, x, y, z, this.sprites, 0f);
        }

    }

}
