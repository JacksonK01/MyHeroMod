package net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.HoldableAbility;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;

public class Float extends HoldableAbility {

    public Float() {
        super(5, 10, "Float", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        player.setNoGravity(true);
        player.getServerWorld().spawnParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY(), player.getZ(),
                3, 0.3, 0, 0.3, 0.01);
        quirk.setCooldown(quirk.getCooldown() + 1);
    }

    @Override
    protected void deActivate(ServerPlayerEntity player, Quirk quirk) {
        super.deActivate();
        player.setNoGravity(false);
    }
}
