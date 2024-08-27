package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.electrification;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HumanStunGun extends ActiveAbility implements ITickAbility {

    private boolean isActive = false;

    public HumanStunGun(@NotNull IAbilityUser user) {
        super(user, "Human Stun Gun", "Defensively discharge electricity, electrocuting anyone within close range.", AbilityCategory.DEFENSE);
    }

    @Override
    public void execute(boolean isKeyDown) {
        isActive = isKeyDown;
    }

    @Override
    public void onStartTick() {
        if(isActive) {
            run();
        }
    }

    private void run() {
        LivingEntity entity = getEntity();
        ServerWorld serverWorld = getServerWorld();
        LivingEntity attacker = entity.getAttacker();

        if(attacker == null) {
            return;
        }

        AffectAll.withinRadius(LivingEntity.class, serverWorld, attacker.getPos(), 2, 1, 2).exclude(entity).with(entityToAffect -> {
            entityToAffect.setVelocity(0, 0, 0);
            entityToAffect.velocityModified = true;
        });
    }
}
