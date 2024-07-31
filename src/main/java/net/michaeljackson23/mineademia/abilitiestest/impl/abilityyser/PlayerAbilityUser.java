package net.michaeljackson23.mineademia.abilitiestest.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.impl.abilityset.AbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IPlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitiestest.usage.TestAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerAbilityUser extends AbilityUser implements IPlayerAbilityUser {

    private TestAbility ability;

    public PlayerAbilityUser(@NotNull ServerPlayerEntity entity) {
        super(entity);

        ability = new TestAbility(this);
    }

    @Override
    public @NotNull ServerPlayerEntity getEntity() {
        return (ServerPlayerEntity) super.getEntity();
    }

    @Override
    public @NotNull IAbilitySet getAbilitySet() {
        return new AbilitySet(ability);
    }

    @Override
    public int getMaxStamina() {
        return 1000;
    }

}
