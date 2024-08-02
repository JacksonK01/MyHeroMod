package net.michaeljackson23.mineademia.abilitysystem.intr.abilityset;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A unique set of abilities
 */
public interface IAbilitySet extends Set<IAbility> {

    @Nullable
    <T extends IActiveAbility> T getByType(@NotNull Class<T> type);

}
