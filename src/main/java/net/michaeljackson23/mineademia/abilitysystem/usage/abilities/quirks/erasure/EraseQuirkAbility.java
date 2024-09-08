package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.erasure;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IRightClickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class EraseQuirkAbility extends ToggleAbility implements ICooldownAbility, IRightClickAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME = 60;

    public static final int DEFAULT_FOV = 60;
    public static final float MAX_RANGE = 80;

    public static final float MAX_RAYCAST_DISTANCE = 1;


    private final Cooldown cooldown;
    private final HashSet<IAbilityUser> blockedUsers;

    public EraseQuirkAbility(@NotNull IAbilityUser user) {
        super(user, "Erase Quirk", DESCRIPTION, Networking.C2S_ABILITY_ONE, AbilityCategory.UTILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
        this.blockedUsers = new HashSet<>();
    }

    @Override
    public boolean executeStart() {
        return true;
    }

    @Override
    public boolean executeEnd() {
        return true;
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        AffectAll<LivingEntity> targets = AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), MAX_RANGE).exclude(entity).exclude(this::isOutsideFov);

    }

    @Override
    public boolean onRightClick(boolean isKeyDown) {
        return false;
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private boolean isOutsideFov(@NotNull LivingEntity target) {
        LivingEntity entity = getEntity();

        Vec3d direction = target.getEyePos().subtract(entity.getEyePos());
        double distance = direction.length();

        Vec3d forward = entity.getRotationVecClient().normalize();

        float angle = Mathf.Vector.angleBetweenVectors(forward, direction.normalize());
        if (angle < getAngle())
            return true;

        HitResult hit = entity.raycast(distance, 0, false);
        return hit == null || hit.getPos().squaredDistanceTo(target.getPos()) > MAX_RAYCAST_DISTANCE;
    }

    private float getAngle() {
        return DEFAULT_FOV;
    }

    private void unblockAll() {
        for (IAbilityUser user : blockedUsers) {
            
        }
    }

}
