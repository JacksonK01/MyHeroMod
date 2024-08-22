package net.michaeljackson23.mineademia.entity.projectile.tornado;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Random;

public class TornadoProjectile extends ThrownItemEntity {
    private int timer = 0;
    private boolean tornadoMode = false;
    private final Random random = new Random();

    public TornadoProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(true);
    }

    public TornadoProjectile(World world, LivingEntity owner) {
        super(EntityRegister.TORNADO_PROJECTILE, owner, world);
        setNoGravity(true);
    }

    public TornadoProjectile(World world, double x, double y, double z) {
        super(EntityRegister.TORNADO_PROJECTILE, x, y, z, world);
        setNoGravity(true);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        if(timer >= 150) {
            kill();
            return;
        }
        if(!(getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        if(getOwner() == null && !(getOwner() instanceof LivingEntity)) {
            return;
        }
        timer++;
        if(tornadoMode) {
            tornadoParticles(serverWorld);
            aoe();
        } else {
            nonTornadoParticle(serverWorld);
        }
//        setVelocity(getTicksToHit().multiply(0.001));
//        this.velocityModified = true;

        //TODO use a loop to make the tornado bigger the higher up it goes

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        this.setVelocity(0, 0, 0);
        velocityModified = true;
        tornadoMode = true;
    }

    private void aoe() {
        AreaOfEffect.execute((LivingEntity) getOwner(), 16, 8, getX(), getY() + 8, getZ(), (entityToEffect) -> {
            QuirkDamage.doEmitterDamage((LivingEntity) getOwner(), entityToEffect, 4f);
            entityToEffect.setVelocity(random.nextDouble(), random.nextDouble(), random.nextDouble());
            entityToEffect.velocityModified = true;
        });
    }

    private void tornadoParticles(ServerWorld serverWorld) {
        int MAX_HEIGHT = 10;
        for(int dy = 0; dy < MAX_HEIGHT; dy++) {
            double delta = dy * 0.5;
            int scale = dy/2;
            double width = 1 + delta;
            double height = 1;
            serverWorld.spawnParticles(ParticleTypes.CLOUD,
                    getX(), getY() + dy, getZ(),
                    20 * scale,
                    width, height, width, 0.1);

            serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK,
                    getX(), getY() + dy, getZ(),
                    5 * scale,
                    width, height, width, 1);

            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    getX(), getY() + dy, getZ(),
                    scale,
                    width, height, width, 1);
        }
    }

    private void nonTornadoParticle(ServerWorld serverWorld) {
        serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                getX(), getY(), getZ(),
                1,
                0.3, 0.3, 0.3, 0);
        serverWorld.spawnParticles(ParticleTypes.CLOUD,
                getX(), getY(), getZ(),
                2,
                0, 0, 0, 0);
    }
}
