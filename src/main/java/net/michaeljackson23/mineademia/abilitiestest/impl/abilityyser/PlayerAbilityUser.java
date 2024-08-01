package net.michaeljackson23.mineademia.abilitiestest.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitiestest.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IPlayerAbilityUser;
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

    public void incrementIndex() {
        if (++index >= getAbilities().size())
            index = 0;

        getEntity().sendMessage(Text.literal("Ability: " + getAbilities().values().stream().toList().get(index).getName()));
    }

    public Class<? extends IAbility> getCurrentAbility() {
        return getAbilities().keySet().stream().toList().get(index);
    }

}
