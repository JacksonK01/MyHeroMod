package net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges;

import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.michaeljackson23.mineademia.quirk.abilities.PassiveAbility;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class FaJin extends BasicAbility {
    private int storedPower = 0;
    private boolean hasAlreadyCrouched = false;

    private int counter = 0;
    private final PassiveAbility particles = ((player, quirk) -> {
        if(counter >= 20) {
            counter = 0;
            return true;
        }
        counter++;
        player.getServerWorld().spawnParticles(
                new DustParticleEffect(new Vector3f(0.85f, 0.0f, 0.0f), 2f),
                player.getX(), player.getY() + 1, player.getZ(),
                10, 0.4, 0.4, 0.4, 0
        );
        player.getServerWorld().spawnParticles(
                ParticleTypes.CLOUD,
                player.getX(), player.getY() + 1, player.getZ(),
                5, 0.4, 0.4, 0.4, 0
        );
        if(counter % 3 == 0) {
            player.getServerWorld().spawnParticles(
                    ModParticles.SHOCKWAVE_PARTICLES,
                    player.getX(), player.getY() + 1, player.getZ(),
                    1, 0, 0, 0, 0
            );
        }
        return false;
    });

    public FaJin() {
        super(80, 100, 120, "Fa-Jin", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        if(player.isSneaking() && !hasAlreadyCrouched) {
            storedPower++;
            hasAlreadyCrouched = true;
            Vec3d playerVec = player.getRotationVector().multiply(0.25).multiply(-1);
            player.getServerWorld().spawnParticles(
                    new DustParticleEffect(new Vector3f(0.85f, 0.0f, 0.0f), 1f),
                    player.getX() + playerVec.x, player.getY() + playerVec.y, player.getZ() + playerVec.z,
                    storedPower * 2, 0.2, 0.4, 0.2, 0
            );
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_HURT, SoundCategory.PLAYERS, 2f, 2f);
        } else if(!player.isSneaking()) {
            hasAlreadyCrouched = false;
        }
        player.sendMessage(Text.literal("Kinetic Energy: " + storedPower).withColor(0xC10000), true);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate();
        player.sendMessage(Text.literal("Release!").withColor(0xC10000), true);
        player.setVelocity(player.getRotationVector().multiply(storedPower));
        player.velocityModified = true;
        player.getServerWorld().spawnParticles(
                ParticleTypes.EXPLOSION,
                player.getX(), player.getY() + 0.1, player.getZ(),
                1, 0, 0, 0, 0
        );
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2f, 2f);
        quirk.addPassive(particles);
        hasAlreadyCrouched = false;
        storedPower = 0;
    }
}
