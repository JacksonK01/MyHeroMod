package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.tornado.TornadoProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TornadoAbility extends ActiveAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 30;
    public static final int STAMINA = 210;


    private final Cooldown cooldown;

    public TornadoAbility(@NotNull IAbilityUser user) {
        super(user, "Magnitude 5", "User focuses wind to form a Tornado.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isReadyAndReset() && getStamina() >= STAMINA) {
            offsetStamina(-STAMINA);
            spawnTornado();
        }

    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void spawnTornado() {
        LivingEntity entity = getEntity();
        World world = entity.getWorld();

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 1f, 2f);

        TornadoProjectile tornadoProjectile = new TornadoProjectile(world, entity);
        tornadoProjectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 1.0f, 0);

        world.spawnEntity(tornadoProjectile);
        entity.swingHand(entity.getActiveHand(), true);
    }
}
