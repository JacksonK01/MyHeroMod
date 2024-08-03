package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.tornado.TornadoProjectile;
import net.michaeljackson23.mineademia.entity.projectile.windblade.WindBladeProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class TornadoAbility extends ActiveAbility implements ICooldownAbility, IStaminaAbility {

    private final Cooldown cooldown;

    public TornadoAbility(@NotNull IAbilityUser user) {
        super(user, "Magnitude 5", "User focuses wind to form a Tornado.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(30);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isReadyAndReset()) {
            spawnTornado();
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 120;
    }

    private void spawnTornado() {
        LivingEntity user = getEntity();
        user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 1f, 2f);
        TornadoProjectile tornadoProjectile = new TornadoProjectile(user.getWorld(), user);
        tornadoProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0f, 1.0f, 0);
        user.getWorld().spawnEntity(tornadoProjectile);

        user.swingHand(user.getActiveHand(), true);
    }
}
