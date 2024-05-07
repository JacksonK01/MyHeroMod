package net.michaeljackson23.mineademia.entity.projectile.airforce;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.util.PlaceParticleInWorld;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Random;

public class AirForceProjectile extends ThrownItemEntity {

    @Override
    protected Item getDefaultItem() {
        return Items.AIR.asItem();
    }
    public AirForceProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public AirForceProjectile(World world, LivingEntity owner) {
        super(EntityRegister.AIR_FORCE_PROJECTILE, owner, world);
    }

    public AirForceProjectile(World world, double x, double y, double z) {
        super(EntityRegister.AIR_FORCE_PROJECTILE, x, y, z, world); // null will be changed later
    }

    public void tick() {
        super.tick();
        this.getWorld().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        Random random = new Random();
        PlaceParticleInWorld.run(this.getWorld(), ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 1, 1, 3);
        //This crashed the game
//        ServerWorld world = (ServerWorld) this.getWorld();
//        world.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 3, 0.3f, 0.3f, 0.3f, 0);
//        world.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
//        int i = entity instanceof BlazeEntity ? 3 : 0; // sets i to 3 if the Entity instance is an instance of BlazeEntity
//        DamageType damageType = new DamageType("Damage", DamageScaling.ALWAYS, 2f, DamageEffects.HURT, DeathMessageType.DEFAULT);
//        DamageSource damageSource = new DamageSource(RegistryEntry.of(damageType));
//        entity.damage(damageSource, 5f);

        if (entity instanceof LivingEntity livingEntity) { // checks if entity is an instance of LivingEntity (meaning it is not a boat or minecart)
            livingEntity.playSound(SoundEvents.BLOCK_ANVIL_HIT, 2F, 1F); // plays a sound for the entity hit only
        }
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) { // checks if the world is client
            this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3, World.ExplosionSourceType.TNT);
            this.kill(); // kills the projectile
        }

    }


}
