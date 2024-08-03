package net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public interface IPlayerAbilityUser extends IAbilityUser {

    @Override
    @NotNull
    ServerPlayerEntity getEntity();

}
