package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Note:
 * Please overview {@link ExampleAbility1} before getting here, thankss :D

 * It appears now we have a new interface: ICooldownAbility, our previous ability had no cooldown... we could use it as often as we wanted to with no restriction, let's fix that!


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
public class ExampleAbility2 extends ActiveAbility implements ICooldownAbility {

    /**
     * "Professionals have standards"
     *      - Sniper, Tf2

     * I'd like to think we're professionals, so let's set up standards, we will keep out tidy list on the very top of each class we go through
     * We do not like "Magic numbers", numbers and values written in code seemingly for no reason
     * This is why we use constants with indicative names to be able to tell the meaning of said value
     * Keeps us nice and tidy yk?

     * Let's decide on our cooldown time! 100....? what? Well in MC we measure time in ticks and therefore 100 ticks, is the (ideal) equivalent of 100 / 20 = 5 seconds!
     */
    public static final int COOLDOWN_TIME = 100;


    /**
     * Notice our variable is 'final', that means it can ONLY be changed in our ability constructor
     * This is a good thing in this case, since our cooldown class should never change

     * If a variable is never meant to change, keep it final

     * Also notice that our variable is private, we will put all our variables as private at all times, in the case in which other classes need them we will make corresponding get and set methods
     */
    private final Cooldown cooldown;

    public ExampleAbility2(@NotNull IAbilityUser user) {
        super(user, "Example Ability 2", "Now with cooldowns!");

        /**
         * We used our constant variable to set up the time!

         * We will later see cooldown times are a dynamic thing that can be changed in the case of need, but for now let's leave it at that
         */
        this.cooldown = new Cooldown(COOLDOWN_TIME);
    }

    /**
     * Notice that method {@link ICooldownAbility#isCooldownReadyAndReset()}, it is essentially a combination of {@link ICooldownAbility#isCooldownReady()} and {@link ICooldownAbility#resetCooldown()}, who'd have thought :P
     * It does exactly what it sounds like, if our cooldown is ready, it resets it and returns 'true', however, if it isn't ready, it just returns 'false'

     * You might be asking: Why are we even checking the cooldown ourselves? We implemented {@link ICooldownAbility} and set up our cooldown why that?
     * To which I answer, in some cases certain abilities may require a cooldown, HOWEVER, will also require extra actions when the cooldown isn't ready, to be as flexible as I can,
     * I've decided to make cooldown checks manual, so that if you wanna add your own twist to it, you can, but if you don't a simple check method's got you covered

     * So this time, our ability only works if our cooldown of {@value COOLDOWN_TIME} is ready!
      * @param isKeyDown Is the key pressed(true) or released(false)
     */
    @Override
    public void execute(boolean isKeyDown) {
        if (isKeyDown && !isCooldownReadyAndReset())
            return;

        if (isKeyDown)
            getEntity().sendMessage(Text.literal("Key Down!"));
        else
            getEntity().sendMessage(Text.literal("Key Up!"));
    }

    /**
     * Let's return the cooldown we just created in the constructor

     * Note that the order of methods is not random, to keep things neat, we will override execution related methods before extra ones (such as cooldown)

     * Also note we annotate {@link Override} and {@link NotNull}, that is because we override this method from our {@link ICooldownAbility} interface, and because we can 100% be sure it will never return null
     * Annotating may seem useless, but it really helps the compiler gives us heads up regarding nulls and overrides and other checks we might miss, and it makes coding more convenient for all of us
     * @return Our Cooldown :D
     */
    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

}
