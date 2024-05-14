package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.util.RaycastToEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
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
        if (blockHit == null) {
            this.entityHit = RaycastToEntity.raycast(player, DISTANCE);
        }
        if (entityHit == null) {
            this.blockHit = player.raycast(21, 1.0f, false);
        }

        if(entityHit != null) {
            player.teleport(player.getServerWorld(), entityHit.getPos().getX(), entityHit.getPos().getY(), entityHit.getPos().getZ(), player.getYaw(), player.getPitch());
        } else if(blockHit != null) {
            player.getServerWorld().createExplosion(player, blockHit.getPos().getX(), blockHit.getPos().getY(), blockHit.getPos().getZ(), 1.0f, World.ExplosionSourceType.BLOCK);
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
