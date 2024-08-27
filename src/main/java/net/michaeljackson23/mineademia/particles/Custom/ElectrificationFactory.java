package net.michaeljackson23.mineademia.particles.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class ElectrificationFactory extends SpriteBillboardParticle {

    public SpriteProvider spriteSet;

    public ElectrificationFactory(ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz, SpriteProvider spriteSet) {
        super(clientWorld, x, y, z, dx, dy, dz);
        this.spriteSet = spriteSet;
        this.velocityMultiplier = 1f;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = 0.2f;
        this.maxAge = 3;
        this.setSprite(spriteSet.getSprite(Random.create()));
    }

    @Override
    public void tick() {
        this.setSprite(spriteSet.getSprite(Random.create()));
        super.tick();
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
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
            return new CowlingFactory(world, x, y, z, velocityX, velocityY, velocityZ, this.sprites);
        }
    }
}
