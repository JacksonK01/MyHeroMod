package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;


import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.PhaseAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.IRightClickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive.HairAmmoAbility;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LoadAmmoAbility extends PhaseAbility implements ICooldownAbility, IRightClickAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME = 10;

    public static final int MAX_BULLET_AMOUNT = 5;

    public static final int DURATION_SOUND_HAIR_TEAR = 17;
    public static final int DURATION_SOUND_BELT = 16;
    public static final int DURATION_SOUND_RIFLE = 36;

    public static final int DURATION_DELAY_HAIR = 40 + DURATION_SOUND_HAIR_TEAR;
    public static final int DURATION_DELAY_BELT = 20 + DURATION_SOUND_BELT;


    private final Cooldown cooldown;

    private AmmoType selectedAmmoType;
    private final HashMap<AmmoType, Integer> ammoMap;
    private int ammoAmount;

    private boolean fromBelt;
    private boolean toBelt;

    public LoadAmmoAbility(@NotNull IAbilityUser user) {
        super(user, "Load Ammo", DESCRIPTION, Networking.C2S_ABILITY_THREE, AbilityCategory.UTILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);

        this.selectedAmmoType = AmmoType.REGULAR;
        this.ammoMap = new HashMap<>();
        this.ammoAmount = 0;

        setPhaseMethods(0, this::fromPhase, this::toPhase);
        setStartPhaseMethods(0, this::fromPhaseStart, this::toPhaseStart);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!isKeyDown)
            return;

        if (getPhase() >= 0 || !isCooldownReady())
            return;

        LivingEntity entity = getEntity();
        if (entity.isSneaking())
            changeAmmoType();
        else
            loadAmmo(false);
    }

    @Override
    public boolean onRightClick(boolean isKeyDown) {
        if (getPhase() >= 0 || !isCooldownReady())
            return false;

        return loadAmmo(true);
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void fromPhase() {
        incrementTicks();
        if (getTicks() >= (fromBelt ? DURATION_DELAY_BELT : DURATION_DELAY_HAIR))
            nextPhase();
    }
    private void fromPhaseStart() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        if (fromBelt) {
            ammoMap.put(selectedAmmoType, ammoMap.get(selectedAmmoType) - 1);
            ammoAmount--;

            world.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_RIFLE_FROM_BELT, SoundCategory.MASTER, 0.25f, 1);
        } else {
            HairAmmoAbility hairAmmoAbility = getUser().getAbility(HairAmmoAbility.class);
            if (hairAmmoAbility == null || !hairAmmoAbility.tryRemoveAmmo(selectedAmmoType.getHairCost())) {
                resetPhase();
                return;
            }

            world.playSound(null, entity.getBlockPos(), ModSounds.QUIRK_RIFLE_TEAR_HAIR, SoundCategory.MASTER, 0.25f, 1);
        }
    }

    private void toPhase() {
        incrementTicks();
        if (getTicks() >= (toBelt ? DURATION_SOUND_BELT : DURATION_SOUND_RIFLE)) {

            if (toBelt) {
                ammoMap.put(selectedAmmoType, ammoMap.getOrDefault(selectedAmmoType, 0) + 1);
                ammoAmount++;
            } else {
                FireRifleAbility fireRifleAbility = getUser().getAbility(FireRifleAbility.class);
                if (fireRifleAbility != null) {
                    fireRifleAbility.setAmmoLoaded(true);
                    fireRifleAbility.setAmmoType(selectedAmmoType);
                }
            }

            resetCooldown();
            resetPhase();
        }
    }
    private void toPhaseStart() {
        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) entity.getWorld();

        world.playSound(null, entity.getBlockPos(), toBelt ? ModSounds.QUIRK_RIFLE_FROM_BELT : ModSounds.QUIRK_RIFLE_RELOAD, SoundCategory.MASTER, 0.25f, 1);
    }

    private boolean loadAmmo(boolean isRightClick) {
        FireRifleAbility fireRifleAbility = getUser().getAbility(FireRifleAbility.class);
        if (fireRifleAbility == null)
            return false;

        boolean ammoInRifle = fireRifleAbility.isAmmoLoaded();

        boolean hasSelectedAmmo = ammoMap.getOrDefault(selectedAmmoType, 0) > 0;
        boolean fullAmmo = ammoAmount >= MAX_BULLET_AMOUNT;

        if (ammoInRifle && fullAmmo || (!isRightClick && fullAmmo))
            return false;

        if ((ammoInRifle && isRightClick))
            return false;

        fromBelt = isRightClick && hasSelectedAmmo;// && !ammoInRifle && hasSelectedAmmo;
        toBelt = !isRightClick;// ammoInRifle;

        nextPhase();
        return true;
    }
    private void changeAmmoType() {
        this.selectedAmmoType = selectedAmmoType.getNext();

        // INFORM PLAYER in a nicer way
        LivingEntity entity = getEntity();
        if (entity instanceof ServerPlayerEntity player) {

            player.sendMessage(Text.literal("Ammo Type: " + this.selectedAmmoType.name()), true);
        }
    }


    public enum AmmoType {

        REGULAR(10, 40, 5, 5),
        HOLLOW_POINT(15, 55, 10, 0);

        private final float hairCost;

        private final float damage;
        private final int ticksToHit;
        private final int pierceLevel;

        AmmoType(float hairCost, float damage, int ticksToHit, int pierceLevel) {
            this.hairCost = hairCost;

            this.damage = damage;
            this.ticksToHit = ticksToHit;
            this.pierceLevel = Math.max(0, pierceLevel);
        }

        public float getHairCost() {
            return hairCost;
        }

        public float getDamage() {
            return damage;
        }

        public int getTicksToHit() {
            return ticksToHit;
        }

        public byte getPierceLevel() {
            return (byte) pierceLevel;
        }

        public AmmoType getNext() {
            AmmoType[] ammoTypes = values();
            int nextIndex = (ordinal() + 1) % ammoTypes.length;

            return ammoTypes[nextIndex];
        }

    }

}
