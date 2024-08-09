package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.*;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;

public class OneForAll extends Quirk {
    //About 5 minutes
    private final int DANGER_SENSE_INTERVAL = 20 * 60 * 5;
    private int dangerSenseCounter = DANGER_SENSE_INTERVAL;

    public OneForAll() {
        super("One For All", new AirForce(), /* new PickVestigeAbility() */ new Empty(), new ShootStyleKicks(), new ManchesterSmash(), new Cowling());
    }

    @Override
    public void tick(ServerPlayerEntity player) {
        super.tick(player);
        dangerSense(player);
    }

    private void dangerSense(ServerPlayerEntity player) {
        if(dangerSenseCounter >= DANGER_SENSE_INTERVAL) {
            AreaOfEffect.execute(player, 8, 2, player.getX(), player.getY(), player.getZ(), (entityToAffect -> {
                if(player.canSee(entityToAffect) &&
                        (entityToAffect instanceof HostileEntity || player.getAttacker() == entityToAffect)) {
                    player.getServerWorld().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.DANGER_SENSE, SoundCategory.PLAYERS, 1f, 1f);
                    Vec3d playerVec = player.getRotationVector();
                    player.getServerWorld().spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                            player.getX() + playerVec.x, player.getEyeY() + playerVec.y, player.getZ() + playerVec.z,
                            1, 0.1, 0, 0.1, 0);
                    dangerSenseCounter = 0;
                }
            }));
        }

        if (dangerSenseCounter < DANGER_SENSE_INTERVAL) {
            dangerSenseCounter++;
        }
    }
}
