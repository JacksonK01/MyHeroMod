package net.michaeljackson23.mineademia.abilitysystem.usage.abilities;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class DodgeAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    public static final float DASH_STRENGTH = 2f;
    public static final float Y_OFFSET = 0.15f;


    private final Cooldown cooldown;

    private Vec3d oldPos;

    private boolean isDashing;
    private int ticks;

    public DodgeAbility(@NotNull IAbilityUser user) {
        super(user, "dodge", "dah dodge", AbilityCategory.MOBILITY);
        this.cooldown = new Cooldown(40);
    }

    @Override
    public void execute() {
        if (!isReadyAndReset())
            return;

        oldPos = getEntity().getPos();

        isDashing = true;
        ticks = 0;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void onTick() {

        if(isDashing && ticks >= 2) {
            LivingEntity entity = getEntity();

            Vec3d newPos = entity.getPos();

            Vec3d velocity = newPos.subtract(oldPos); // new Vec3d(newPos.getX() - oldPos.getX() , 0, newPos.getZ() - oldPos.getZ());
            if(velocity.length() == 0)
                velocity = entity.getRotationVecClient();

            velocity = velocity.normalize().multiply(DASH_STRENGTH);
            velocity = new Vec3d(velocity.x, Y_OFFSET, velocity.z);

            entity.setVelocity(velocity);
            entity.velocityModified = true;

            isDashing = false;
            ticks = 0;
        }

        ticks++;
    }
}
