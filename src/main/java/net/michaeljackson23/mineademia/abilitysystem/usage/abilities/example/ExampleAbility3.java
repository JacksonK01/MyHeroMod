package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.ToggleAbility;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.active.HoldAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Note:
 * Please overview {@link ExampleAbility2} before getting here, thankss :D

 * Toggle??
 * Yeah we already have that built in! A toggle ability is just what it sounds like... you turn it on... and then you turn it off
 * {@link HoldAbility} is EXACTLY IDENTICAL (I am not even joking), no changes are made in implementations and all changes are internal, so apply what you learned here for that
 * Awesome! Let's begin

 * Oh we also will cover some stamina stuff in here too, no new standards really, ig make sure the executes come before the ticks yk?

 * Standards:
 *      - No "Magic numbers": Set up constant variables with meaningful names at the top of the class, separated from the rest of the class by 2 line spaces
 *      - Final variables: If a variable is never meant to change, keep it final
 *      - Private variables: All instance variables are to always be private, to access them from the outside(Only if needed), use get and set methods
 *      - Variable naming: All static variables will be written in 'SCREAM_SNAKE_CASE', all other variables will be written in 'camelCase'
 *      - Order of methods: Execution methods come before extra ones, public methods come before private ones, static methods go at the bottom, separated from the rest of the class by 2 line spaces
 *      - Method naming: All methods will be written in 'camelCase'
 *      - Method parameters: In a method where the return value is never null, annotate {@link NotNull}, if a method might return null, annotate {@link Nullable}
 *                           Similarly, if a parameters must never be null, annotate {@link NotNull} and if it can be null, annotate {@link Nullable}
 *                           Annotate {@link Override} above methods you override from super classes / interfaces

 * THOSE WHO DO NOT FOLLOW STANDARDS SHALL BE SMITTEN!!!
 * Nah jk but I'll prob throw ya a comment and worst case revise your code(Please don't make me revise your code I'd feel bad) cause those stuff really matters in the long run yk? (Please follow standards :3)
 */
public class ExampleAbility3 extends ToggleAbility implements ICooldownAbility {

    public static final int COOLDOWN_TIME = 100;

    public static final int INITIAL_STAMINA = 50;
    public static final int STAMINA_PER_TICK = 1;

    public static final int MESSAGE_FREQUENCY = 200;
    public static final int MAX_TIME = 1000;


    private final Cooldown cooldown;

    public ExampleAbility3(@NotNull IAbilityUser user) {
        super(user, "Example Ability 3", "My third ability!");

        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }


    /**
     * What's that? a BOOLEAN return???
     * When using toggle abilities, the return value of the boolean determines whether the ability was successfully toggled on, {@link ToggleAbility#executeStart()} is called when you attempt to toggle an ability on
     * If we return true the ability is toggled on as it should be, returning false cancels the toggle, that can be used to stop toggling in some conditions, in our example if the cooldown isn't ready we stop the toggle
     * @return Do we allow toggle(true) or cancel it(false)
     */
    @Override
    public boolean executeStart() {
        if (!isCooldownReady() || !hasStamina(INITIAL_STAMINA))
            return false;

        getEntity().sendMessage(Text.literal("Power: ON"));
        return true;
    }

    /**
     * Similarly, {@link ToggleAbility#executeEnd()} is called when you toggle an ability off.
     * And just like last time, true makes things continue as usual(toggle off) ond false cancels it(stays on)

     * Note:
     * We didn't instantly reset the cooldown on start, rather, we checked on start and reset on end
     * We don't have to do it this way, it really is up to you, but that's how I'd do it
     * @return Do we allow toggle(true) or cancel it(false)
     */
    @Override
    public boolean executeEnd() {
        getEntity().sendMessage(Text.literal("Power: OFF"));

        resetCooldown();
        return true;
    }

    /**
     * This method is called every tick if the ability is toggled on
     * We use a modulo to send a message every set amount of time

     * In this case we stop the ability is {@value MAX_TIME} has passed, or if you have less stamina than {@value INITIAL_STAMINA}
     * Note that we offset stamina by a NEGATIVE amount because offset ADDS stamina

     * The {@link ToggleAbility#getTicks()} method shows us how much time had passed since we toggled on the ability, similarly, once the ability turns off they represent how much time has passed since we toggled off the ability
     */
    @Override
    public void onTickActive() {
        int ticks = getTicks();

        if (ticks % MESSAGE_FREQUENCY == 0)
            getEntity().sendMessage(Text.literal("10 seconds have passed since the last message!"));

        offsetStamina(-STAMINA_PER_TICK);

        if (ticks >= MAX_TIME || !hasStamina(INITIAL_STAMINA))
            setActive(false);
    }

    /**
     * Similarly, this method is called every tick if the ability is toggled off
     * In our case usage we do not need it, but sometimes it's very useful to have it
     */
    @Override
    public void onTickInactive() { }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

}
