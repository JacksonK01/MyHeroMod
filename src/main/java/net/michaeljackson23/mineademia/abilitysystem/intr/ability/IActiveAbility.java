package net.michaeljackson23.mineademia.abilitysystem.intr.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Base class of all active abilities
 */
public interface IActiveAbility extends IAbility {

    @NotNull
    HashSet<AbilityCategory> getCategories();

    @NotNull
    HashSet<AbilityCategory> getBlockedCategories();
    void setBlockedCategories(@NotNull AbilityCategory @NotNull... categories);

    boolean isBlockIgnoreSelf();
    void setBlockIgnoreSelf(boolean blockIgnoreSelf);

    default void setBlockedCategories(boolean blockIgnoreSelf, @NotNull AbilityCategory @NotNull ... categories) {
        setBlockedCategories(categories);
        setBlockIgnoreSelf(blockIgnoreSelf);
    }

    default boolean isConflicting() {
        IAbilityUser user = getUser();

        for (IAbility ability : user.getAbilities().values()) {
            if (!(ability instanceof IActiveAbility activeAbility) || (isBlockIgnoreSelf() && ability.equals(this)))
                continue;

            // If any overlap between the blocking state and the categories, is conflicting
            if (getCategories().stream().anyMatch(activeAbility.getBlockedCategories()::contains))
                return true;
        }

        return false;
    }

    @Override
    default boolean canExecute() {
        return !isConflicting();
    }

    @Override
    default void encode(@NotNull PacketByteBuf buffer) {
        IAbility.super.encode(buffer);

    }

}
