package net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class Blackwhip extends BasicAbility {
    private EntityHitResult entityHit;
    private BlockHitResult blockHit;
    private final int DISTANCE = 20;
    PassiveAbility blockHitPassive = ((player, quirk) -> {
        //This passive still exists after ability is done, that's why the first if checks if block hit null
        if(blockHit == null) {
            return true;
        } else if (player.isSneaking()) {
            reset = true;
            deActivate(player, quirk);
            return true;
        } else if (!isConnectedStage()) {
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

        double dy = Math.abs(player.getY()) - Math.abs(blockHit.getPos().getY());

        double velocityX = 0;
        double velocityY = 0;
        double velocityZ = 0;

        double distance = Math.sqrt(Math.pow(distanceZ, 2) + Math.pow(distanceY, 2) + Math.pow(distanceX, 2));
        double multiplier = distance / DISTANCE;
        double k = .05;
        double rx = blockHit.getPos().getX();
        double rz = blockHit.getPos().getZ();
        double forcex = -k * Math.abs(player.getPos().x - rx);
        double forcez = -k * Math.abs(player.getPos().z - rz);
        double move = player.getMovementSpeed();
        if (player.getPos().y < blockHit.getPos().getY()) {
            if (Math.abs(distanceX) >= 5 && dy < 0) {
                if (blockHit.getPos().x <= player.getX()) {
                    velocityX = (forcex);
                }
                if (blockHit.getPos().x >= player.getX()) {
                    velocityX = -(forcex);

                }
            } else if (dy > 0) {
                reset = true;
                deActivate(player, quirk);
                return true;
            }
            if (Math.abs(distanceY) >= 5) {
                velocityY = ((distanceY * -1) * multiplier / 20);
            }
            if (Math.abs(distanceZ) >= 5 && dy < 0) {
                if (blockHit.getPos().z <= player.getZ()) {
                    velocityZ = (forcez);

                }
                if (blockHit.getPos().z >= player.getZ()) {
                    velocityZ = -(forcez);
                }
            }
        }
        if (dy >= 0) {
            reset = true;
            deActivate(player, quirk);
            return true;
        }
        if (velocityX != 0 || velocityY != 0 || velocityZ != 0) {
            player.setVelocityClient(player.getVelocity().x + velocityX, player.getVelocity().y + velocityY, player.getVelocity().z + velocityZ);
            player.velocityModified = true;

        }
        return false;
    });
    Stages stage = Stages.INIT;
    //This signals to deactivate that the ability should be reset to default
    private boolean reset = false;

    public Blackwhip() {
        super(1, 10, 10, "Blackwhip", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(isConnectedStage()) {
            stage = Stages.PULL;
        }

        if (isInitStage()) {
            this.blockHit = (BlockHitResult) player.raycast(DISTANCE, 1.0f, false);
            if(!player.getServerWorld().isAir(blockHit.getBlockPos())) {
                Vec3d vec = player.getRotationVec(1.0f);
                player.setVelocity(player.getVelocity().x + vec.x,
                        player.getVelocity().y,
                        player.getVelocity().z + vec.z);
                player.velocityModified = true;

                stage = Stages.CONNECTED;
                quirk.addPassive(blockHitPassive);
                } else {
                reset = true;
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
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate(player, quirk);
        if (this.reset) {
            this.stage = Stages.INIT;
            this.entityHit = null;
            this.blockHit = null;
            this.reset = false;
        }
    }

    private enum Stages {
        INIT,
        CONNECTED,
        PULL;
    }
}
