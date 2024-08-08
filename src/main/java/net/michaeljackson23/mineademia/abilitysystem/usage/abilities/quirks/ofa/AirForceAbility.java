package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.entity.projectile.airforce.AirForceProjectile;
import net.michaeljackson23.mineademia.util.PlayerAngleVector;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class AirForceAbility extends ActiveAbility implements ICooldownAbility {
    private static final int STAMINA = 100;
    private static final int COOLDOWN = 5;

    private final Cooldown cooldown;

    public AirForceAbility(@NotNull IAbilityUser user) {
        super(user, "Air Force", "Concentrate your power to release into a powerful gust of wind", AbilityCategory.ATTACK);
        this.cooldown = new Cooldown(COOLDOWN);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isReadyAndReset() && isKeyDown && getStamina() >= STAMINA) {
            offsetStamina(-STAMINA);
            spawnAirforce();
        }
    }

    private void spawnAirforce() {
        LivingEntity entity = getEntity();

        AirForceProjectile airForceProjectile = new AirForceProjectile(entity.getWorld(), entity);
        airForceProjectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0f, 1f, 0);
        entity.getWorld().spawnEntity(airForceProjectile);

        entity.swingHand(entity.getActiveHand(), entity instanceof ServerPlayerEntity);

        Vec3d vec3d = getVec3d(entity).multiply(-1);
        entity.setVelocity(vec3d.x, entity.getVelocity().y, vec3d.z);
        entity.velocityModified = true;
    }

    private Vec3d getVec3d(LivingEntity entity) {
        double yawRad = Math.toRadians(entity.getYaw());
        double x = -Math.sin(yawRad);
        double z = Math.cos(yawRad);
        return new Vec3d(x, 0, z).normalize();
    }
}
