package net.michaeljackson23.mineademia.abilities.ofa;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.PassiveAbility;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.block.Block;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class Blackwhip extends AbilityBase {
    private EntityHitResult entityHit;
    private HitResult blockHit;
    private final int DISTANCE = 20;
    PassiveAbility blockHitPassive;
    Stages stage = Stages.INIT;
    //This signals to deactivate that the ability should be reset to default
    private boolean reset = false;

    private Blackwhip() {
        super(1, 10, 10, false, "Blackwhip", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        if(isConnectedStage()) {
            if(blockHit != null) {
                stage = Stages.PULL;
            } else {
                reset = true;
            }
        }

        if (blockHit == null && isInitStage()) {
            blockHit = player.raycast(DISTANCE, 1.0f, false);
        }

        if(blockHit != null && blockHitPassive == null) {
            BlockHitResult castedBlockHit = (BlockHitResult) blockHit;
            if(player.getServerWorld().isAir(castedBlockHit.getBlockPos())) {
                reset = true;
            } else {
                Vec3d vec = player.getRotationVec(1.0f);
                player.setVelocity(player.getVelocity().x + vec.x,
                        player.getVelocity().y,
                        player.getVelocity().z + vec.z);
                player.velocityModified = true;

                stage = Stages.CONNECTED;
                blockHitPassive = () -> {
                    if (player.isSneaking()) {
                        reset = true;
                        deactivate();
                        return true;
                    } else if (isPullStage()) {
                        return true;
                    }

                    int numberOfParticles = 50;
                    double stepSize = 1.0 / numberOfParticles;

                    double playerX = player.getX();
                    double playerY = player.getY() + 1;
                    double playerZ = player.getZ();
                    for (double t = 0; t <= 1.0; t += stepSize) {
                        double x = playerX + t * (blockHit.getPos().getX() - playerX);
                        double y = playerY + t * (blockHit.getPos().getY() - playerY);
                        double z = playerZ + t * (blockHit.getPos().getZ() - playerZ);

                        player.getServerWorld().spawnParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 1, 0.0f, 0.0f, 0.0f, 0);
                    }

                    double distanceX = player.getX() - blockHit.getPos().getX();
                    double distanceY = player.getY() - blockHit.getPos().getY();
                    double distanceZ = player.getZ() - blockHit.getPos().getZ();

                    double velocityX = 0;
                    double velocityY = 0;
                    double velocityZ = 0;

                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                    double multiplier = distance / DISTANCE;

                    if (Math.abs(distanceX) >= 5) {
                        velocityX = (distanceX * -1) * multiplier / 20;
                    }

                    if (Math.abs(distanceY) >= 5) {
                        velocityY = (distanceY * -1) * multiplier / 20;
                    }

                    if (Math.abs(distanceZ) >= 5) {
                        velocityZ = (distanceZ * -1) * multiplier / 20;
                    }

                    if (velocityX != 0 || velocityY != 0 || velocityZ != 0) {
                        player.addVelocity(velocityX, velocityY, velocityZ);
                        player.velocityModified = true;
                    }

                    return false;
                };
                playerData.getPassives().add(blockHitPassive);
            }
        }

        if (isPullStage()) {
            Vec3d vec = player.getRotationVec(1.0f);
            Vec3d vec2 = new Vec3d(vec.x * 0.5, 0.5, vec.z * 0.5);
            player.setVelocity(player.getVelocity().x + vec2.x,
                    player.getVelocity().y + vec2.y,
                    player.getVelocity().z + vec2.z);
            player.velocityModified = true;
            reset = true;
        }
     }

     private boolean isInitStage() {
        return stage == Stages.INIT;
     }

    private boolean isConnectedStage() {
        return stage == Stages.CONNECTED;
    }

    private boolean isPullStage() {
        return stage == Stages.PULL;
    }

    @Override
    protected void deactivate() {
        super.deactivate();
        if (this.reset) {
            this.stage = Stages.INIT;
            this.entityHit = null;
            this.blockHit = null;
            this.blockHitPassive = null;
            this.reset = false;
        }
    }

    public static AbilityBase getInstance() {
        return new Blackwhip();
    }

    private enum Stages {
        INIT,
        CONNECTED,
        PULL;
    }
}
