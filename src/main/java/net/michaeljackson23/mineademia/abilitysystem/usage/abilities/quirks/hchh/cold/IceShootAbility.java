package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class IceShootAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    private final Cooldown cooldown;
    private int ticks;

    public IceShootAbility(@NotNull IAbilityUser user) {
        super(user, "Ice Shoot", "Shoots several beams of ice from the user", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(20);
        this.ticks = 11;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isCooldownReadyAndReset())
            ticks = 0;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void onStartTick() {
        if (ticks <= 10) {
            if (ticks % 2 == 0) {
                LivingEntity entity = getEntity();
                World world = entity.getWorld();

                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2f, 2f);

                IceProjectile iceProjectile = new IceProjectile(world, entity);
                iceProjectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 0.5f, 1);
                iceProjectile.setPosition(iceProjectile.getX(), iceProjectile.getY() - 1, iceProjectile.getZ());

                world.spawnEntity(iceProjectile);
                entity.swingHand(Hand.OFF_HAND, true);
            }

            ticks++;
        }
    }
}
