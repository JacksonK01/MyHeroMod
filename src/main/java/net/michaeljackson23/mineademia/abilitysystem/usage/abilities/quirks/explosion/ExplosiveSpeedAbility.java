package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.explosion;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class ExplosiveSpeedAbility extends HoldAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 60;

    public static final int INTERVAL_TIME = 5;
    public static final Vec3d STRENGTH = new Vec3d(1, 0.75f, 1);


    private final Cooldown cooldown;

    private boolean dashing;
    private int ticks;

    public ExplosiveSpeedAbility(@NotNull IAbilityUser user) {
        super(user, "Explosive Speed", "The user throws their hands back and then causes an explosion in their palms to propel themselves forward. This move can also be used to achieve a pseudo form of flight.", AbilityCategory.MOBILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    @Override
    public boolean executeStart() {
        if (!isReady())
            return false;

        dashing = true;
        ticks = 0;
        return true;
    }

    @Override
    public void executeEnd() {
        dashing = false;
        reset();
    }

    @Override
    public boolean onTickActive() {
        if (dashing) {
            if (ticks++ >= INTERVAL_TIME) {
                moveUser();
                ticks = 0;
            }
        }

        return true;
    }

    @Override
    public void onTickInactive() { }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void moveUser() {
        LivingEntity entity = getEntity();

        Vec3d velocity = entity.getRotationVecClient().normalize();

        velocity = velocity.normalize().multiply(STRENGTH);
        velocity = new Vec3d(velocity.x, velocity.y, velocity.z);

        entity.setVelocity(velocity);
        entity.velocityModified = true;
    }

}
