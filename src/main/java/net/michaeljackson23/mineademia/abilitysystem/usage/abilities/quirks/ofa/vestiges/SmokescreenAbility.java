package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.vestiges;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.michaeljackson23.mineademia.util.AreaOfEffect;
import net.michaeljackson23.mineademia.util.DrawParticles;
import net.michaeljackson23.mineademia.util.QuirkDamage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

//UNFINISHED
public class SmokescreenAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {

    private static final int COOLDOWN = 10;

    private final Cooldown cooldown;

    private boolean isActive = false;

    public SmokescreenAbility(@NotNull IAbilityUser user) {
        super(user, "Smokescreen", "Allows the user to generate a thick cloud of purple smoke from their body.", AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public void onStartTick() {
        if(isActive) {
            smokeScreen();
        }
    }

    private void smokeScreen() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        AffectAll.withinRadius(LivingEntity.class, world, entity.getPos(), 3, 1, 3).exclude(entity).with((entityToAffect -> {
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 25, 1, true, false, true));
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 2, true, false, true));
            entityToAffect.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 40, 1, true, false, true));
            QuirkDamage.doEmitterDamage(entity, entityToAffect, 0.5f);
        }));

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 2f, 2f);

        DrawParticles.spawnParticles(world, new DustParticleEffect(new Vector3f(0.5f, 0.0f, 0.5f), 2.0f),
                entity.getPos().add(0, 1, 0),
                35, 1.5f, 1f, 1.5f, 1, true);

        getEntity().sendMessage(Text.literal("Trying to do smokescreen"));
    }

    @Override
    public void execute(boolean isKeyDown) {
        getEntity().sendMessage(Text.literal("Trying to do smokescreen"));
        isActive = isKeyDown;
    }
}
