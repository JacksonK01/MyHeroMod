package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.minecraft.text.Text;

/**
 * Base class of all hoold abilities, uses the {@link ITickAbility} class to tick while active or inactive
 */
public interface IHoldAbility extends IActiveAbility, ITickAbility {


    boolean isHeld();
    void setHeld(boolean toggled);

    boolean executeStart();
    void executeEnd();

    boolean onTickActive();
    void onTickInactive();

    @Override
    default void execute(boolean isKeyDown) {
        getEntity().sendMessage(Text.literal(  "Is Held:" + isHeld()));
        if (isHeld() && !isKeyDown) {
            getEntity().sendMessage(Text.literal(  "Set Held False"));
            executeEnd();
            setHeld(false);
        } else if (isKeyDown && executeStart()) {
            getEntity().sendMessage(Text.literal(  "Set Held True"));
            setHeld(true);
        }
    }

    @Override
    default void onTick() {
        getEntity().sendMessage(Text.literal(  "Tick"));
        if (isHeld()) {
            if (!onTickActive()) {
                executeEnd();
                setHeld(false);
            }
        } else
            onTickInactive();
    }


}
