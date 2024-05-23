package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.util.RaycastToEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Blackwhip extends AbilityBase {
    private EntityHitResult entityHit;
    private HitResult blockHit;
    private final int DISTANCE = 20;

    private Blackwhip() {
        super(100, 10, 10, false, "Blackwhip", "Insert title");
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        player.sendMessage(Text.literal("Blackwhip"));
        if (blockHit == null) {
            this.blockHit = player.raycast(DISTANCE, 1.0f, false);
        }

        if(blockHit != null) {
            int numberOfParticles = 50;
            double stepSize = 1.0 / numberOfParticles;

            double playerX = player.getX();
            double playerY = player.getY() + 1;
            double playerZ = player.getZ();
            for (double t = 0; t <= 1.0; t += stepSize) {
                double x = playerX + t * (blockHit.getPos().getX() + 0.5 - playerX);
                double y = playerY + 1 + t * (blockHit.getPos().getY() + 0.5 - playerY);
                double z = playerZ + t * (blockHit.getPos().getZ() + 0.5 - playerZ);

                player.getServerWorld().spawnParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 1, 0.0f, 0.0f, 0.0f, 0);
            }
        }
     }

    @Override
    protected void deactivate() {
        this.entityHit = null;
        this.blockHit = null;
    }

    public static AbilityBase getInstance() {
        return new Blackwhip();
    }
}
