package net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public interface IPlayerAbilityUser extends IAbilityUser {

    @Override
    @NotNull
    ServerPlayerEntity getEntity();

}
