package net.michaeljackson23.mineademia.abilitiestest.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IPlayerAbilityUser;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerAbilityUser extends AbilityUser implements IPlayerAbilityUser {

    public PlayerAbilityUser(@NotNull ServerPlayerEntity entity) {
        super(entity);
    }

    @Override
    public @NotNull ServerPlayerEntity getEntity() {
        return (ServerPlayerEntity) super.getEntity();
    }

    @Override
    public int getMaxStamina() {
        return 1000;
    }

}
