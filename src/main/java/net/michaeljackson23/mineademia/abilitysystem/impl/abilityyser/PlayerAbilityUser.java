package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IPlayerAbilityUser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class PlayerAbilityUser extends AbilityUser implements IPlayerAbilityUser {

    private int index;

    public PlayerAbilityUser(@NotNull ServerPlayerEntity entity) {
        super(entity);
        this.index = 0;
    }

    @Override
    public @NotNull ServerPlayerEntity getEntity() {
        return (ServerPlayerEntity) super.getEntity();
    }

    @Override
    public int getMaxStamina() {
        return 1000;
    }

    @Override
    public int getStaminaRegenAmount() {
        return 1;
    }

    @Override
    public int getStaminaRegenRate() {
        return 1;
    }

    public void incrementIndex() {
        if (++index >= getAbilities().size())
            index = 0;

        getEntity().sendMessage(Text.literal("Ability: " + getAbilities().values().stream().toList().get(index).getName()));
    }

    public Class<? extends IAbility> getCurrentAbility() {
        return getAbilities().keySet().stream().toList().get(index);
    }

}
