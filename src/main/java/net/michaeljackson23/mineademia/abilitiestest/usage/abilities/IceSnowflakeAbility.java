package net.michaeljackson23.mineademia.abilitiestest.usage.abilities;

import net.michaeljackson23.mineademia.abilitiestest.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitiestest.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceProjectile;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceSnowflakeProjectile;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

public class IceSnowflakeAbility extends ActiveAbility implements ICooldownAbility{
    private Cooldown cooldown;
    public IceSnowflakeAbility(@NotNull IAbilityUser user) {
        super(user, "Ice Snowflake", "Shoots a beam of ice that splits into several smaller ones", AbilityCategory.ATTACK);
    }
    @Override
    protected void init() {
        super.init();
        cooldown = new Cooldown(20);
    }
    @Override
    public void execute() {
        if(isReadyAndReset()){
            var entity = getUser().getEntity();
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 2f, 2f);
            IceSnowflakeProjectile iceProjectile = new IceSnowflakeProjectile(entity.getWorld(), entity);
            iceProjectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 0.5f, 1);
            iceProjectile.setPosition(iceProjectile.getX(), iceProjectile.getY() - 1, iceProjectile.getZ());
            entity.getWorld().spawnEntity(iceProjectile);
            entity.swingHand(Hand.OFF_HAND, true);
        }

    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }
}
