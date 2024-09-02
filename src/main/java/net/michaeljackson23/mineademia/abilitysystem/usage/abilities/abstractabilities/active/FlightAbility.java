package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.active;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.Mathf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FlightAbility extends ToggleAbility {

    private Vec3d pushVector;
    private Vec3d pushAdder;
    private int pushRate;

    private boolean canHover;
    private float hoverDescent;
    private float hoverDescentSneaking;

    private int flyStaminaCost;
    private int hoverStaminaCost;

    private boolean ignoreFallDmg;

    private Vec3d startPush;
    private Vec3d endPush;

    private int pushCounter;

    public FlightAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, @NotNull AbilityCategory @NotNull ... categories) {
        super(user, name, description, defaultIdentifier, categories);

        this.pushVector = Mathf.Vector.ONE;
        this.pushAdder = Vec3d.ZERO;
        this.pushRate = 1;

        this.canHover = false;
        this.hoverDescent = 0;
        this.hoverDescentSneaking = 0;

        this.flyStaminaCost = 0;
        this.hoverStaminaCost = 0;

        this.ignoreFallDmg = true;

        this.startPush = Vec3d.ZERO;
        this.endPush = Vec3d.ZERO;

        this.pushCounter = 0;
    }

    @Override
    public boolean executeStart() {
        if (startPush.lengthSquared() != 0) {
            LivingEntity entity = getEntity();

            entity.setVelocity(startPush);
            entity.velocityModified = true;
        }

        return true;
    }

    @Override
    public boolean executeEnd() {
        if (endPush.lengthSquared() != 0) {
            LivingEntity entity = getEntity();

            entity.setVelocity(endPush);
            entity.velocityModified = true;
        }

        return true;
    }

    @Override
    public void onTickActive() {
        LivingEntity entity = getEntity();

        if (ignoreFallDmg)
            entity.fallDistance = 0;

        if (canHover && !entity.isSprinting())
            hover();
        else
            fly();

    }

    private void fly() {
        LivingEntity entity = getEntity();

        if (pushCounter-- > 0 || entity.isOnGround())
            return;
        if (!hasStaminaAndConsume(flyStaminaCost))
            return;

        Vec3d push = getFlightVector();

        entity.setVelocity(push);
        entity.velocityModified = true;

        onFly();
        pushCounter = pushRate;
    }

    private void hover() {
        LivingEntity entity = getEntity();

        if (!hasStaminaAndConsume(hoverStaminaCost))
            return;

        if (entity instanceof ServerPlayerEntity player) {
            float yValue = entity.isSneaking() ? hoverDescentSneaking : hoverDescent;

            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeVec3d(new Vec3d(Double.MAX_VALUE, -yValue, Double.MAX_VALUE));

            ServerPlayNetworking.send(player, Networking.S2C_SET_VELOCITY, buffer);
        } else
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 254, false, false, false));

        onHover();
    }

    @NotNull
    protected Vec3d getFlightVector() {
        LivingEntity entity = getEntity();
        return entity.getRotationVecClient().normalize().add(pushAdder).multiply(pushRate);
    }

    protected void onFly() { }
    protected void onHover() { }


    public Vec3d getPushVector() {
        return pushVector;
    }
    public void setPushVector(@NotNull Vec3d pushVector) {
        this.pushVector = pushVector;
    }

    public Vec3d getPushAdder() {
        return pushAdder;
    }
    public void setPushAdder(@NotNull Vec3d pushAdder) {
        this.pushAdder = pushAdder;
    }

    public int getPushRate() {
        return pushRate;
    }
    public void setPushRate(int pushRate) {
        this.pushRate = Math.max(1, pushRate);
    }

    public boolean isCanHover() {
        return canHover;
    }
    public void setCanHover(boolean canHover) {
        this.canHover = canHover;
    }

    public float getHoverDescent() {
        return hoverDescent;
    }
    public void setHoverDescent(float hoverDescent) {
        this.hoverDescent = hoverDescent;
    }

    public float getHoverDescentSneaking() {
        return hoverDescentSneaking;
    }
    public void setHoverDescentSneaking(float hoverDescentSneaking) {
        this.hoverDescentSneaking = hoverDescentSneaking;
    }

    public int getFlyStaminaCost() {
        return flyStaminaCost;
    }
    public void setFlyStaminaCost(int flyStaminaCost) {
        this.flyStaminaCost = Math.max(0, flyStaminaCost);
    }

    public int getHoverStaminaCost() {
        return hoverStaminaCost;
    }
    public void setHoverStaminaCost(int hoverStaminaCost) {
        this.hoverStaminaCost = Math.max(0, hoverStaminaCost);
    }

    public boolean isIgnoreFallDmg() {
        return ignoreFallDmg;
    }
    public void setIgnoreFallDmg(boolean ignoreFallDmg) {
        this.ignoreFallDmg = ignoreFallDmg;
    }

    public Vec3d getStartPush() {
        return startPush;
    }
    public void setStartPush(@NotNull Vec3d startPush) {
        this.startPush = startPush;
    }

    public Vec3d getEndPush() {
        return endPush;
    }
    public void setEndPush(@NotNull Vec3d endPush) {
        this.endPush = endPush;
    }

}
