package net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerAbilityUser extends IAbilityUser {

    @Override
    @NotNull
    ServerPlayerEntity getEntity();

    @Nullable
    <T extends IActiveAbility> Class<T> getBoundAbility(@NotNull Identifier identifier);

    <T extends IActiveAbility> void setBoundAbility(@NotNull Identifier Identifier, @NotNull IActiveAbility ability);

}
