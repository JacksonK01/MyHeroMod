package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceBeamProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class IceBeamAbility extends ActiveAbility implements ICooldownAbility {

    private final Cooldown cooldown;

    public IceBeamAbility(@NotNull IAbilityUser user) {
        super(user, "Ice Beam", "Shoots a beam of ice", AbilityCategory.ATTACK);
        cooldown = new Cooldown(20);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (isReadyAndReset()) {
            LivingEntity entity = getUser().getEntity();
            World world = entity.getWorld();

            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2f, 2f);

            IceBeamProjectile iceProjectile = new IceBeamProjectile(world, entity);
            iceProjectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 1.15f, 0);
            iceProjectile.setPosition(iceProjectile.getX(), iceProjectile.getY(), iceProjectile.getZ());

            world.spawnEntity(iceProjectile);
            entity.swingHand(Hand.OFF_HAND, true);
        }

    }
}
