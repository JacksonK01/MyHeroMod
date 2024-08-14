package net.michaeljackson23.mineademia.abilitysystem.usage.abilities;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

//TODO make the player invincible to attack damage for a few ticks
public class DodgeAbility extends ActiveAbility implements ICooldownAbility, ITickAbility {

    public static final float DASH_STRENGTH = 2f;
    public static final float Y_OFFSET = 0.15f;


    private final Cooldown cooldown;

    private Vec3d oldPos;

    private boolean dashing;
    private int ticks;
    private boolean isAir;

    public DodgeAbility(@NotNull IAbilityUser user) {
        super(user, "Dodge", "dah dodge", AbilityCategory.MOBILITY);
        this.cooldown = new Cooldown(40);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!isCooldownReadyAndReset())
            return;

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        isAir = world.getBlockState(entity.getBlockPos().down()).isAir();

        if(isKeyDown && isAir) {
            AnimationProxy.sendAnimationToClients(entity, "air_dodge");
        } else {
            AnimationProxy.sendAnimationToClients(entity, "ground_dodge");
        }

        oldPos = getEntity().getPos();

        dashing = true;
        ticks = 0;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void onStartTick() {
        if(dashing) {
            if (ticks++ >= 2) {
                LivingEntity entity = getEntity();

                Vec3d newPos = entity.getPos();

                Vec3d velocity = newPos.subtract(oldPos);
                if (velocity.length() == 0)
                    velocity = entity.getRotationVecClient();

                velocity = velocity.normalize().multiply(DASH_STRENGTH);
                velocity = new Vec3d(velocity.x, Y_OFFSET, velocity.z);

                entity.setVelocity(velocity);

                if(isAir) {
                    entity.setVelocity(0, 0.25, 0);
                    entity.fallDistance = 0f;
                }

                entity.velocityModified = true;

                dashing = false;
                ticks = 0;
            }
        }
    }
}
