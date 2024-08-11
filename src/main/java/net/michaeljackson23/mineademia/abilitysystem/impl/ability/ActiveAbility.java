package net.michaeljackson23.mineademia.abilitysystem.impl.ability;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public abstract class ActiveAbility extends Ability implements IActiveAbility {

    private final HashSet<AbilityCategory> categories;
    private final HashSet<AbilityCategory> blockedCategories;

    private final Identifier defaultIdentifier;

    private boolean blockedIgnoreSelf;

    public ActiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @Nullable Identifier defaultIdentifier, @NotNull AbilityCategory @NotNull... categories) {
        super(user, name, description);

        this.categories = new HashSet<>(List.of(categories));
        this.blockedCategories = new HashSet<>();

        this.defaultIdentifier = defaultIdentifier;

        this.blockedIgnoreSelf = true;
    }
    public ActiveAbility(@NotNull IAbilityUser user, @NotNull String name, @NotNull String description, @NotNull AbilityCategory @NotNull... categories) {
        this(user, name, description, null, categories);
    }

    @Override
    public @NotNull HashSet<AbilityCategory> getCategories() {
        return categories;
    }

    @Override
    public @NotNull HashSet<AbilityCategory> getBlockedCategories() {
        return blockedCategories;
    }

    @Override
    public boolean isBlockIgnoreSelf() {
        return blockedIgnoreSelf;
    }

    @Override
    public void setBlockIgnoreSelf(boolean blockIgnoreSelf) {
        this.blockedIgnoreSelf = blockIgnoreSelf;
    }

    @Override
    public void setBlockedCategories(@NotNull AbilityCategory @NotNull... categories) {
        blockedCategories.clear();
        blockedCategories.addAll(List.of(categories));
    }

    @Override
    public @Nullable Identifier getDefaultIdentifier() {
        return defaultIdentifier;
    }

}
