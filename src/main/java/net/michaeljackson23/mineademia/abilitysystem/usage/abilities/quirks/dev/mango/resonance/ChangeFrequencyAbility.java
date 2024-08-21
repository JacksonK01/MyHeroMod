package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.dev.mango.resonance;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public class ChangeFrequencyAbility extends ActiveAbility {

    private boolean constructive;

    public ChangeFrequencyAbility(@NotNull IAbilityUser user) {
        super(user, "Change Frequency", "", AbilityCategory.UTILITY);
    }


    @Override
    public void execute(boolean isKeyDown) {
        if (isKeyDown)
            constructive = !constructive;
    }

    public boolean isConstructive() {
        return constructive;
    }

}
