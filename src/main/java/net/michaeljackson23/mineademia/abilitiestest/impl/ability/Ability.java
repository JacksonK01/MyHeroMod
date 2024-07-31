package net.michaeljackson23.mineademia.abilitiestest.impl.ability;

import net.michaeljackson23.mineademia.abilitiestest.impl.Abilities;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public abstract class Ability implements IAbility {

    private final IAbilityUser user;

    private final String name;
    private final String description;

    private boolean active;

    public Ability(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description) {
        this.user = user;

        this.name = name;
        this.description = description;

        this.active = true;

        init();
        Abilities.registerAbility(this);
    }

    @Override
    public @NotNull IAbilityUser getUser() {
        return user;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    protected void init() { }

}
