package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceSnowflakeProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class IceSnowflakeAbility extends ActiveAbility implements ICooldownAbility{

    private final Cooldown cooldown;

    public IceSnowflakeAbility(@NotNull IAbilityUser user) {
        super(user, "Ice Snowflake", "Shoots a beam of ice that splits into several smaller ones", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(20);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (isCooldownReadyAndReset()) {
            LivingEntity entity = getEntity();
            World world = entity.getWorld();

            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2f, 2f);

            IceSnowflakeProjectile iceProjectile = new IceSnowflakeProjectile(world, entity);
            iceProjectile.setVelocity(entity, -90, 0f, 0f, 0.5f, 1);
            iceProjectile.setPosition(iceProjectile.getX(), iceProjectile.getY() - 1, iceProjectile.getZ());

            world.spawnEntity(iceProjectile);
            entity.swingHand(Hand.OFF_HAND, true);
        }

    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }
}
