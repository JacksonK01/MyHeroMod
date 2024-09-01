package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.util.AffectAll;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RifleSmack extends ActiveAbility implements ICooldownAbility, ITickAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME_HIT = 60;
    public static final int COOLDOWN_TIME_MISS = 20;

    public static final int STAMINA_COST_HIT = 20;
    public static final int STAMINA_COST_MISS = 10;

    public static final int DELAY = 10;
    public static final float RANGE = 2f;
    public static final float DAMAGE = 5f;
    public static final float PUSH_STRENGTH = 0.5f;


    private final Cooldown cooldown;
    private int ticks;

    private LivingEntity target;

    public RifleSmack(@NotNull IAbilityUser user) {
        super(user, "Rifle Smack", DESCRIPTION, Networking.C2S_ABILITY_FOUR, AbilityCategory.ATTACK);

        this.cooldown = new Cooldown(COOLDOWN_TIME_HIT);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!isCooldownReady() || !hasStamina(STAMINA_COST_HIT))
            return;

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        Vec3d pos = entity.getPos();

        Optional<LivingEntity> targetOptional = AffectAll.withinRadius(LivingEntity.class, world, pos, RANGE).exclude(entity).getFirst();
        if (targetOptional.isEmpty()) {
            offsetStamina(-STAMINA_COST_MISS);

            cooldown.setCooldownTicks(COOLDOWN_TIME_MISS);
            cooldown.reset();

            return;
        }

        this.target = targetOptional.get();

        Vec3d push = target.getPos().subtract(pos).normalize().multiply(PUSH_STRENGTH);

        target.setVelocity(push);

        target.damage(world.getDamageSources().mobAttack(entity), DAMAGE);

        offsetStamina(-STAMINA_COST_HIT);

        cooldown.setCooldownTicks(COOLDOWN_TIME_HIT);
        cooldown.reset();

        ticks = 0;
    }

    @Override
    public void onStartTick() {
        if (ticks >= 0 && ticks++ >= DELAY) {
            shootRifle();
            ticks = -1;
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void shootRifle() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        FireRifleAbility fireRifleAbility = getUser().getAbility(FireRifleAbility.class);
        SuperchargedShotAbility superchargedShotAbility = getUser().getAbility(SuperchargedShotAbility.class);
        if (superchargedShotAbility != null && fireRifleAbility != null && fireRifleAbility.isAmmoLoaded()) {
            world.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_RIFLE_SHOOT, SoundCategory.MASTER, 0.5f, 1);

            float shotDamage = superchargedShotAbility.getDamage(fireRifleAbility.getAmmoType());
            fireRifleAbility.setAmmoLoaded(false);

            target.damage(world.getDamageSources().mobAttack(entity), shotDamage);
        } else {
            world.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_RIFLE_SHOOT_EMPTY, SoundCategory.MASTER, 0.25f, 1);
        }
    }

}
