package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.miscellaneous;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuperHeroLandingAbility extends ActiveAbility {

    public SuperHeroLandingAbility(@NotNull IAbilityUser user) {
        super(user, "Super Hero Landing", "For testing", AbilityCategory.ANIMATION);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown) {
            AnimationProxy.sendAnimationToClients(getEntity(), "super_hero_landing");
            isKeyDown = false;
        }
    }
}
