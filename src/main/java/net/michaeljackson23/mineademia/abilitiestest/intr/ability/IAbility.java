package net.michaeljackson23.mineademia.abilitiestest.intr.ability;

import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all abilities
 */
public interface IAbility {

    @NotNull
    IAbilityUser getUser();

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    void execute();

    boolean isActive();
    void setActive(boolean active);

}
