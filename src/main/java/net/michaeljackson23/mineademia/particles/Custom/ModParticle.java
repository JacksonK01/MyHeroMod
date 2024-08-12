package net.michaeljackson23.mineademia.particles.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModParticle extends SpriteBillboardParticle  {

    private final SpriteProvider spriteProvider;
    private final ParticleTextureSheet sheetType;

    protected ModParticle(@NotNull ClientWorld world, @NotNull Vec3d pos, @NotNull Vec3d velocity, @NotNull SpriteProvider spriteProvider, @NotNull ParticleTextureSheet sheetType, float scale, int maxAge, float velocityMultiplier, float upwardsAcceleration) {
        super(world, pos.x, pos.y, pos.z);

        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;

        this.velocityX = velocity.x;
        this.velocityY = velocity.y;
        this.velocityZ = velocity.z;

        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);

        this.sheetType = sheetType;

        this.scale = scale;
        this.maxAge = maxAge;

        this.velocityMultiplier = velocityMultiplier;
        this.gravityStrength = -upwardsAcceleration;
    }

    @Override
    public ParticleTextureSheet getType() {
        return sheetType;
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
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<T> createFactory(@NotNull ParticleTextureSheet sheetType, float scale, int maxAge, float velocityMultiplier, float upwardsAcceleration) {
        return provider -> (ParticleFactory<T>) new FactoryResult(provider, sheetType, scale, maxAge, velocityMultiplier, upwardsAcceleration);
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<T> createFactory(@NotNull ParticleTextureSheet sheetType, float scale, int maxAge) {
        return provider -> (ParticleFactory<T>) new FactoryResult(provider, sheetType, scale, maxAge);
    }

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends ParticleEffect> ParticleFactoryRegistry.PendingParticleFactory<T> createFactory(float scale, int maxAge) {
        return provider -> (ParticleFactory<T>) new FactoryResult(provider, scale, maxAge);
    }


    @Environment(EnvType.CLIENT)
    public static class FactoryResult implements ParticleFactory<DefaultParticleType> {

        private final SpriteProvider spriteProvider;
        private final ParticleTextureSheet sheetType;

        private final float scale;
        private final int maxAge;

        private final float velocityMultiplier;
        private final float upwardsAcceleration;

        public FactoryResult(@NotNull SpriteProvider spriteProvider, @NotNull ParticleTextureSheet sheetType, float scale, int maxAge, float velocityMultiplier, float upwardsAcceleration) {
            this.spriteProvider = spriteProvider;
            this.sheetType = sheetType;

            this.scale = scale;
            this.maxAge = maxAge;

            this.velocityMultiplier = velocityMultiplier;
            this.upwardsAcceleration = upwardsAcceleration;
        }
        public FactoryResult(@NotNull SpriteProvider spriteProvider, @NotNull ParticleTextureSheet sheetType, float scale, int maxAge) {
            this(spriteProvider, sheetType, scale, maxAge, 1, 0);
        }
        public FactoryResult(@NotNull SpriteProvider spriteProvider, float scale, int maxAge) {
            this(spriteProvider, ParticleTextureSheet.PARTICLE_SHEET_LIT, scale, maxAge, 1, 0);
        }

        @Nullable
        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ModParticle(world, new Vec3d(x, y, z), new Vec3d(velocityX, velocityY, velocityZ), spriteProvider, sheetType, scale, maxAge, velocityMultiplier, upwardsAcceleration);
        }

    }

}
