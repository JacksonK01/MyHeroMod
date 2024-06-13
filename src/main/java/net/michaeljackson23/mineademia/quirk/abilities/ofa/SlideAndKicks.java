package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.michaeljackson23.mineademia.util.StopSoundProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SlideAndKicks extends AbilityBase {
    private boolean init = false;
    //Yaw is horizontal
    private float yaw = 0;
    private Vec3d storedVec;
    private boolean isAir;
    private boolean hasAirStarted = false;
    private int airTimer = 0;
    private boolean hasAirAnimationPlayed = false;

    private int kickTimer = 0;
    private boolean secondInit = false;
    private boolean hasHitUp = false;

    private boolean teleportCancel = false;

    private ArrayList<LivingEntity> entityList = new ArrayList<>();

    public SlideAndKicks() {
        super(40, 80, 41, false, "Slide", "test");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(!init) {
            init(player, quirk);
        }
        if(isAir) {
            inAir(player, quirk);
        } else {
            onGround(player, quirk);
        }
    }

    @Override
    protected void deactivate(ServerPlayerEntity player, Quirk quirk) {
        super.deactivate(player, quirk);
        StopSoundProxy.execute(player, CustomSounds.SLIDE_ID, SoundCategory.PLAYERS);
        init = false;
        yaw = 0;
        storedVec = null;
        AnimationProxy.sendStopAnimation(player);
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_FIRST_PERSON, PacketByteBufs.empty());
        quirk.removeModel("Slide");
        QuirkDataPacket.sendProxy(player);
        kickTimer = 0;
        secondInit = false;
        hasHitUp = false;
        isAir = false;
        hasAirStarted = false;
        hasAirAnimationPlayed = false;
        teleportCancel = false;
        airTimer = 0;
        entityList.clear();
    }

    private void init(ServerPlayerEntity player, Quirk quirk) {
        this.isAir = player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir();
        if(!isAir) {
            this.yaw = player.getYaw();
            this.storedVec = getVec3d(player);
            init = true;
            AnimationProxy.sendAnimationToClients(player, "slide");

            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), CustomSounds.SLIDE_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            quirk.addModel("Slide");
            QuirkDataPacket.sendProxy(player);
        } else {
            if(!hasAirAnimationPlayed) {
                AnimationProxy.sendAnimationToClients(player, "air_kick_down");
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 1f, 2f);
                hasAirAnimationPlayed = true;
            }
        }
    }

    private void inAir(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        if(!hasAirStarted) {
            ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
            this.storedVec = player.getRotationVector().multiply(1.2);
            player.setVelocity(storedVec.x, -1, storedVec.z);
            player.velocityModified = true;
            hasAirStarted = true;
            AreaOfEffect.execute(player, 2, 1, player.getX(), player.getY(), player.getZ(), (entity) -> {
                entity.setVelocity(storedVec.x, -1, storedVec.z);
                entity.velocityModified = true;
                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        1, entity.getWidth(), entity.getHeight(), entity.getWidth(),
                        0);
                QuirkDamage.doPhysicalDamage(player, entity, 2.5f);
            });
        }
        if(airTimer >= 14) {
            deactivate(player, quirk);
        }
        airTimer++;
    }

    private void onGround(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.FORCE_INTO_THIRD_PERSON_BACK, PacketByteBufs.empty());
        sendYawPackage(player);
        checkIfJump(player);
        if(wasPressedAgain()) {
            kickFlip(player, quirk);
            if(kickTimer > 25) {
                deactivate(player, quirk);
                return;
            }
            timer--;
            kickTimer++;
        } else {
            slide(player, quirk);
        }
    }

    private void checkIfJump(ServerPlayerEntity player) {
        if(player.getVelocity().y > 0) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, 0, velocity.z);
            player.velocityModified = true;
        }
    }

    private void kickFlip(ServerPlayerEntity player, Quirk quirk) {
        if(player.getServerWorld().getBlockState(player.getBlockPos().down()).isAir()) {
            deactivate(player, quirk);
            return;
        }
        if(!secondInit) {
            AreaOfEffect.execute(player, 4, 1, player.getX() - storedVec.x, player.getY(), player.getZ() - storedVec.z, (entity) -> {
                entity.setVelocity(storedVec.x, 0.5, storedVec.z);
                entity.velocityModified = true;
            });
            StopSoundProxy.execute(player, CustomSounds.SLIDE_ID, SoundCategory.PLAYERS);
            quirk.removeModel("Slide");
            QuirkDataPacket.sendProxy(player);
            AnimationProxy.sendAnimationToClients(player, "from_slide_to_flipkick");
            secondInit = true;
        }
        if(kickTimer <= 10) {
            player.setVelocity(new Vec3d(storedVec.x, player.getVelocity().y, storedVec.z).multiply(0.5, 1, 0.5));
            player.velocityModified = true;
        }
        if(!hasHitUp && kickTimer > 10) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.PLAYERS, 1f, 1f);
            AreaOfEffect.execute(player, 4, 2, player.getX(), player.getY() + 1, player.getZ(), (entity) -> {
                entity.setVelocity(storedVec.x, 1, storedVec.z);
                entity.velocityModified = true;
                player.getServerWorld().spawnParticles(ParticleTypes.EXPLOSION,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        1, entity.getWidth(), entity.getHeight(), entity.getWidth(),
                        0);
                QuirkDamage.doPhysicalDamage(player, entity, 5.0f);
            });
            hasHitUp = true;
        }
    }

    public void slide(ServerPlayerEntity player, Quirk quirk) {
        player.setVelocity(storedVec.x, player.getVelocity().y, storedVec.z);
        player.velocityModified = true;

        Vec3d playerVec = getVec3d(player).multiply(2);
        double futureX = player.getX() + playerVec.x;
        double futureZ = player.getZ() + playerVec.z;

        AreaOfEffect.execute(player, 4, 1, futureX, player.getY() + 0.5, futureZ, (entity) -> {
            if (!entityList.contains(entity)) {
                entityList.add(entity);
            }
        });

        if (!entityList.isEmpty()) {
            Vec3d playerVec2 = getVec3d(player).multiply(2);
            double targetX = player.getX() + playerVec2.x;
            double targetZ = player.getZ() + playerVec2.z;

            entityList.forEach((livingEntity) -> {
                livingEntity.teleport(targetX, player.getY(), targetZ);
                QuirkDamage.doPhysicalDamage(player, livingEntity, 1.0f);
            });
        }

        if (didCollideWithBlock(player)) {
            deactivate(player, quirk);
        }
    }

    private Vec3d getVec3d(ServerPlayerEntity player) {
        double yawRad = Math.toRadians(player.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize();
    }

    private void sendYawPackage(ServerPlayerEntity player) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeFloat(this.yaw);
        ServerPlayNetworking.send(player, Networking.SET_YAW, data);
    }

    private boolean didCollideWithBlock(ServerPlayerEntity player) {
        World world = player.getWorld();
        double DISTANCE = 2;
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d v = getVec3d(player).normalize().multiply(DISTANCE);
        Vec3d end = start.add(v.x * DISTANCE, v.y * DISTANCE, v.z * DISTANCE);
        HitResult result = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        return result.getType() == HitResult.Type.BLOCK;
    }

    private boolean wasPressedAgain() {
        return this.getAmountOfTimesActivated() >= 2;
    }
}
