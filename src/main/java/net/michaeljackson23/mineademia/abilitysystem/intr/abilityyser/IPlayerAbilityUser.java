package net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IPlayerAbilityUser extends IAbilityUser {

    @Override
    @NotNull
    ServerPlayerEntity getEntity();

    @Nullable
    Class<? extends IActiveAbility> getBoundAbility(@NotNull Identifier identifier);

    void setBoundAbility(@NotNull Identifier Identifier, @NotNull Class<? extends IActiveAbility> ability);

    default void executeBoundAbility(@NotNull Identifier identifier, boolean isKeyDown) {
        Class<? extends IActiveAbility> ability = getBoundAbility(identifier);
        if (ability != null)
            execute(ability, isKeyDown);
    }

}
