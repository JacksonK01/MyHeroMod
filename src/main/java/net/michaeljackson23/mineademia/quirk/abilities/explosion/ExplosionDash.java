package net.michaeljackson23.mineademia.quirk.abilities.explosion;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExplosionDash extends AbilityBase {
    int interval = 0;
    final int MAX_INTERVAL = 20;
    public ExplosionDash() {
        super(0, 5, 5, true, "Explosion Dash", "Insert desc");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        double pitch = ((player.getPitch() + 90) * Math.PI) / 180;
        double yaw = ((player.getYaw() + 90) * Math.PI)/ 180;
        double x = Math.cos(yaw) ;
        double y = Math.sin(pitch);
        double z = Math.sin(yaw);

        player.getServerWorld().spawnParticles(ParticleTypes.SMOKE,
                player.getX() + z*.5, player.getY() + 0.7, player.getZ() - x*.5,
                1, 0, 0, 0, 0);

        player.getServerWorld().spawnParticles(ParticleTypes.SMOKE,
                player.getX() - z*.5, player.getY() + 0.7, player.getZ() + x*.5,
                1, 0, 0, 0, 0);

        interval++;
    }

    @Override
    protected void deactivate() {
        super.deactivate();
        interval = 0;
    }

}
