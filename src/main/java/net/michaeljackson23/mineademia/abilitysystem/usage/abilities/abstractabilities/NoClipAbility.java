package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import org.jetbrains.annotations.NotNull;

public class NoClipAbility extends DataPassiveAbility {

    private boolean clipping;

    public NoClipAbility(@NotNull IAbilityUser user) {
        super(user, "No Clip", "Is No Clipping");

        this.clipping = false;
    }

    public boolean isClipping() {
        return clipping;
    }

    public void setClipping(boolean clipping) {
        this.clipping = clipping;
    }

}
