package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.whirlwind;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.windblade.WindBladeProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class WindBladeAbility extends ActiveAbility implements ICooldownAbility {

    private static final int COOLDOWN_TIME = 10;
    private static final int STAMINA = 110;

    private final Cooldown cooldown;

    public WindBladeAbility(@NotNull IAbilityUser user) {
        super(user, "Wind Blade", "User creates a blade of wind that they can actively control.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (isCooldownReadyAndReset() && isKeyDown && getStamina() >= STAMINA) {
            offsetStamina(-STAMINA);
            spawnWindBlade();
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void spawnWindBlade() {
        LivingEntity entity = getEntity();
        World world = entity.getWorld();

        WindBladeProjectile windBlade = new WindBladeProjectile(entity.getWorld(), entity);
        windBlade.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 1f, 0);

        world.spawnEntity(windBlade);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1.0f, 2.0f);
        entity.swingHand(entity.getActiveHand(), entity instanceof ServerPlayerEntity);
    }
}
