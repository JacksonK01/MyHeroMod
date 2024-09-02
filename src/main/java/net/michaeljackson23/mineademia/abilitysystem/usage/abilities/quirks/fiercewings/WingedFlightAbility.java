package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.fiercewings;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.active.FlightAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.fiercewings.passive.FeatherWingsAbility;
import net.michaeljackson23.mineademia.mixin.accessors.EntityAccessor;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class WingedFlightAbility extends FlightAbility {

    public static final String DESCRIPTION = "";

    public static final float MIN_SPEED = 0.25f;
    public static final float MAX_SPEED = 2f;


    public WingedFlightAbility(@NotNull IAbilityUser user) {
        super(user, "Flight", DESCRIPTION, Networking.C2S_ABILITY_TWO, AbilityCategory.MOBILITY, AbilityCategory.ANIMATION);

        setCanHover(true);
        setHoverDescentSneaking(0.05f);
    }

    @Override
    public boolean executeStart() {
        LivingEntity entity = getEntity();
        setStartPush(entity.isSprinting() ? getFlightVector() : Vec3d.ZERO);

        if (entity instanceof ServerPlayerEntity player)
            ((EntityAccessor) player).invokeSetFlag(7, true);

        return super.executeStart();
    }

    @Override
    public boolean executeEnd() {
        if (getEntity() instanceof ServerPlayerEntity player)
            ((EntityAccessor) player).invokeSetFlag(7, false);

        return super.executeEnd();
    }

    @Override
    protected @NotNull Vec3d getFlightVector() {
        FeatherWingsAbility featherWingsAbility = getUser().getAbility(FeatherWingsAbility.class);

        float multiplier = 1;
        if (featherWingsAbility != null)
            multiplier = Mathf.lerp(MIN_SPEED, MAX_SPEED, featherWingsAbility.getPartialValue());

        return super.getFlightVector().multiply(multiplier);
    }

}
