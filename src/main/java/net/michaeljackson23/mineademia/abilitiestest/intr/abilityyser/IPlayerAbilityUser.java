package net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public interface IPlayerAbilityUser extends IAbilityUser {

    @Override
    @NotNull
    ServerPlayerEntity getEntity();

}
