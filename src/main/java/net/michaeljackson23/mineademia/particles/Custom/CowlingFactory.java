package net.michaeljackson23.mineademia.particles.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class CowlingFactory extends SpriteBillboardParticle {
    public SpriteProvider spriteSet;
    public CowlingFactory(ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz, SpriteProvider spriteSet) {
        super(clientWorld, x, y, z, dx, dy, dz);
        this.spriteSet = spriteSet;
        this.velocityMultiplier = 0f;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = 0.14f;
        this.maxAge = 2;
        this.setSprite(spriteSet);
    }

    @Override
    public void tick() {
        this.alpha = Random.create().nextBetween(0, 1);
        super.tick();
    }

    @Override
    public int getBrightness(float tint) {
        float f = ((float)this.age + tint) / (float)this.maxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
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
