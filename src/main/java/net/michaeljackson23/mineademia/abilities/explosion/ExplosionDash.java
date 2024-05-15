package net.michaeljackson23.mineademia.abilities.explosion;

import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.entity.EntityPose;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ExplosionDash extends AbilityBase {
    int interval = 0;
    final int MAX_INTERVAL = 20;
    private ExplosionDash() {
        super(0, 5, 5, true, "Explosion Dash", "Insert desc");
    }

    @Override
    protected void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server) {
        Vec3d playerRotationVec = player.getRotationVec(1.0f).normalize();
        player.getServerWorld().spawnParticles(ParticleTypes.SMOKE,
                player.getX() + playerRotationVec.z * 2, player.getY() + 0.5, player.getZ() - playerRotationVec.x * 2,
                1, 0, 0, 0, 0);

        player.getServerWorld().spawnParticles(ParticleTypes.SMOKE,
                player.getX() - playerRotationVec.z * 2, player.getY() + 0.5, player.getZ() + playerRotationVec.x * 2,
                1, 0, 0, 0, 0);
        interval++;
    }

    @Override
    protected void deactivate() {
        super.deactivate();
        interval = 0;
    }

    public static AbilityBase getInstance() {
        return new ExplosionDash();
    }
}
