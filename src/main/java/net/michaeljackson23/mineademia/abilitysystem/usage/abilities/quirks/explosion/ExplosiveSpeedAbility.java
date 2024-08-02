package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IStaminaAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ExplosiveSpeedAbility extends ActiveAbility implements ICooldownAbility, IStaminaAbility {

    public static final int COOLDOWN_TIME = 10;
    public static final Vec3d STRENGTH = new Vec3d(2, 1, 2);


    private final Cooldown cooldown;

    public ExplosiveSpeedAbility(@NotNull IAbilityUser user) {
        super(user, "Explosive Speed", "The user throws their hands back and then causes an explosion in their palms to propel themselves forward. This move can also be used to achieve a pseudo form of flight.", AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public void execute() {
        if (!isReadyAndReset())
            return;

        LivingEntity entity = getEntity();

        Vec3d velocity = entity.getRotationVecClient().normalize();

        velocity = velocity.normalize().multiply(STRENGTH);
        velocity = new Vec3d(velocity.x, velocity.y, velocity.z);

        entity.setVelocity(velocity);
        entity.velocityModified = true;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public int getStaminaCost() {
        return 25;
    }

}
