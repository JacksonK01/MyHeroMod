package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle;


import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.rifle.passive.HairAmmoAbility;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class LoadAmmoAbility extends ActiveAbility implements ICooldownAbility {

    public static final String DESCRIPTION = "";

    public static final int COOLDOWN_TIME = 0;


    private final Cooldown cooldown;

    private AmmoType selectedAmmoType;

    public LoadAmmoAbility(@NotNull IAbilityUser user) {
        super(user, "Load Ammo", DESCRIPTION, Networking.C2S_ABILITY_THREE, AbilityCategory.UTILITY);

        this.cooldown = new Cooldown(COOLDOWN_TIME);
        this.selectedAmmoType = AmmoType.REGULAR;
    }

    @Override
    public void execute(boolean isKeyDown) {
        if (!isKeyDown)
            return;

        LivingEntity entity = getEntity();
        if (entity.isSneaking())
            changeAmmoType();
        else
            loadAmmo();
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    private void loadAmmo() {
        FireRifleAbility fireRifleAbility = getUser().getAbility(FireRifleAbility.class);
        if (fireRifleAbility == null)
            return;

        if (!isCooldownReady() || fireRifleAbility.isAmmoLoaded())
            return;

        HairAmmoAbility hairAmmoAbility = getUser().getAbility(HairAmmoAbility.class);
        if (hairAmmoAbility == null || !hairAmmoAbility.tryRemoveAmmo(selectedAmmoType.getHairCost()))
            return;

        fireRifleAbility.setAmmoLoaded(true);
        fireRifleAbility.setAmmoType(selectedAmmoType);
    }

    private void changeAmmoType() {
        this.selectedAmmoType = selectedAmmoType.getNext();

        // INFORM PLAYER in a nicer way
        LivingEntity entity = getEntity();
        if (entity instanceof ServerPlayerEntity player)
            player.sendMessage(Text.literal("Ammo Type: " + this.selectedAmmoType.name()));
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
