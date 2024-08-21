package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.ScheduledPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class EnhancedEyesightAbility extends ScheduledPassiveAbility {

    public static final String DESCRIPTION = "Your eyesight is unaffected by neither darkness nor weather conditions, such as heavy rainfall. Your keen eye can also notice entities from very far away, and even track down entities behind walls in a smaller radius for you to curve your shots at.";

    public static final int SCHEDULE_FREQUENCY = 20;
    public static final int EFFECT_TIME = 400;

    public static final int GLOWING_RADIUS_VISIBLE = 100;
    public static final int GLOWING_RADIUS_OBSTRUCTED = 50;


    private final HashSet<Integer> glowingIds;

    public EnhancedEyesightAbility(@NotNull IAbilityUser user) {
        super(user, "Enhanced Eyesight", DESCRIPTION, SCHEDULE_FREQUENCY);

        this.glowingIds = new HashSet<>();
    }

    @Override
    public void execute(boolean isKeyDown) {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, EFFECT_TIME, 1, false, false, true));
        entity.removeStatusEffect(StatusEffects.DARKNESS);

        if (entity instanceof ServerPlayerEntity player) {
            removeGlow();

            AffectAll<LivingEntity> visibleGlow = AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), GLOWING_RADIUS_VISIBLE).exclude(entity).exclude((target) -> !isInLineOfSight(target)).clearGlowingColor().withClientGlowing(player, true);
            visibleGlow.getAll().forEach((e) -> glowingIds.add(e.getId()));

//            AffectAll<LivingEntity> obstructedGlowing = AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), GLOWING_RADIUS_OBSTRUCTED).exclude(entity).exclude(this::isInLineOfSight).with((target) -> GlowingHelper.setColor(target, 1, 1, 0)).withClientGlowing(player, true);
//            obstructedGlowing.getAll().forEach((e) -> glowingIds.add(e.getId()));
        }
    }

    private boolean isInLineOfSight(@NotNull LivingEntity target) {
        LivingEntity entity = getEntity();
        return entity.getWorld().raycast(new RaycastContext(entity.getEyePos(), target.getEyePos(), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.ANY, entity)).getType() == HitResult.Type.MISS;
    }

    private void removeGlow() {
        if (glowingIds.isEmpty())
            return;

        LivingEntity entity = getEntity();
        if (!(entity instanceof ServerPlayerEntity player))
            return;

        PacketByteBuf buffer = PacketByteBufs.create();

        buffer.writeIntArray(glowingIds.stream().mapToInt((i) -> i).toArray());
        buffer.writeBoolean(false);

        ServerPlayNetworking.send(player, Networking.S2C_GLOW_ENTITIES, buffer);

        glowingIds.clear();
    }

}
