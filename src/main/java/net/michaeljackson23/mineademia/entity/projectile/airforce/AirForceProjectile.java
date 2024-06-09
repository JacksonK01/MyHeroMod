package net.michaeljackson23.mineademia.entity.projectile.airforce;

import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.sound.CustomSounds;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Random;

public class AirForceProjectile extends ThrownItemEntity {
    int timer = 0;

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
        super(EntityRegister.AIR_FORCE_PROJECTILE, x, y, z, world);
    }

    public void tick() {
        super.tick();
        this.getWorld().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        PlaceParticleInWorld.spawn(this.getWorld(), ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 1, 1, 3);
        this.setNoGravity(true);
        timer++;
        if(timer > 40) {
            this.kill();
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity) { // checks if entity is an instance of LivingEntity (meaning it is not a boat or minecart)
            livingEntity.playSound(SoundEvents.BLOCK_ANVIL_HIT, 2F, 1F); // plays a sound for the entity hit only
        }
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        getWorld().playSound(null, hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ(), SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.PLAYERS, 1f, 1f);
        this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), 3, World.ExplosionSourceType.TNT);
        this.kill();
    }


}
