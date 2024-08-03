package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.windblade.WindBladeProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;

public class WindBladeAbility extends ActiveAbility implements ICooldownAbility, IStaminaAbility {

    private final Cooldown cooldown;

    public WindBladeAbility(@NotNull IAbilityUser user) {
        super(user, "Wind Blade", "User creates a blade of wind that they can actively control.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(10);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isReadyAndReset()) {
            spawnWindBlade();
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 15;
    }

    private void spawnWindBlade() {
        LivingEntity livingEntity = getUser().getEntity();
        WindBladeProjectile windBlade = new WindBladeProjectile(livingEntity.getWorld(), livingEntity);
        windBlade.setVelocity(livingEntity, livingEntity.getPitch(), livingEntity.getYaw(), 0f, 1f, 0);
        livingEntity.getWorld().spawnEntity(windBlade);
        livingEntity.getWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1.0f, 2.0f);
        livingEntity.swingHand(livingEntity.getActiveHand(), true);
    }
}
