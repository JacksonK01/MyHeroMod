package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.vestiges;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class FloatAbility extends ActiveAbility implements ITickAbility {
    private boolean isActive = false;

    public FloatAbility(@NotNull IAbilityUser user) {
        super(user, "Float", "Allows the user to levitate and suspend themselves in mid-air", AbilityCategory.MOBILITY);
    }

    @Override
    public void onStartTick() {
        if(isActive) {
            doFloat();
        }
    }

    @Override
    public void execute(boolean isKeyDown) {
        LivingEntity user = getEntity();

        user.setNoGravity(isKeyDown);
        isActive = isKeyDown;
    }

    private void doFloat() {
        LivingEntity user = getEntity();
        ServerWorld serverWorld = (ServerWorld) user.getWorld();

        DrawParticles.forWorld(serverWorld).spawnParticles(ParticleTypes.CLOUD, user.getPos(), 3, 0.3f, 0, 0.3f, 0.01f, true);
    }
}
