package net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityset.IAbilitySet;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IPlayerAbilityUser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class PlayerAbilityUser extends AbilityUser implements IPlayerAbilityUser {

    private final HashMap<Identifier, Class<? extends IActiveAbility>> abilityBindMap;
    private int index;

    public PlayerAbilityUser(@NotNull ServerPlayerEntity entity) {
        super(entity);

        this.abilityBindMap = new HashMap<>();
        this.index = 0;
    }

    @Override
    public @NotNull ServerPlayerEntity getEntity() {
        return (ServerPlayerEntity) super.getEntity();
    }

    @Override
    public @Nullable Class<? extends IActiveAbility> getBoundAbility(@NotNull Identifier identifier) {
        return abilityBindMap.get(identifier);
    }

    @Override
    public void setBoundAbility(@NotNull Identifier identifier, @NotNull Class<? extends IActiveAbility> ability) {
        abilityBindMap.put(identifier, ability);
    }

    public void incrementIndex() {
        if (++index >= getAbilities().size())
            index = 0;

        getEntity().sendMessage(Text.literal("Ability: " + getAbilities().values().stream().toList().get(index).getName()));
    }

    public Class<? extends IAbility> getCurrentAbility() {
        return getAbilities().keySet().stream().toList().get(index);
    }

    @Override
    public void setAbilities(@NotNull IAbilitySet abilities) {
        super.setAbilities(abilities);

        for (IAbility ability : abilities) {
            if (!(ability instanceof IActiveAbility activeAbility))
                continue;

            Identifier defaultIdentifier = activeAbility.getDefaultIdentifier();
            if (defaultIdentifier == null)
                continue;

            setBoundAbility(defaultIdentifier, activeAbility.getClass());
        }
    }
}
