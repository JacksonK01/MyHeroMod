package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.example;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * THis is our very first ability!

 * An {@link ActiveAbility} is an ability whose {@link ActiveAbility#execute(boolean)} method is called every time you press or release its corresponding keybind! (We also have passive abilities, but that's for later ^^)
 * To tell apart a press from a release, the boolean 'isKeyDown' indicates whether the ability is pressed down or not

 * Note!
 * Non-Player entities can execute abilities as well, due to them not being able to physically click a keyboard, their value of the 'isKeyDown' boolean may vary depending on the entities attempt to simulate various inputs
 * Make sure to take that into consideration when designing your ability
 */
public class ExampleAbility1 extends ActiveAbility {

    /**
     * Note!
     * The constructor of ALL abilities must only contain an {@link IAbilityUser}, nothing more, nothing less

     * Over here we create our ability!
     * We can set its name, description and even categories which we will explore later, if you have no categories, leave the field blank
     * @param user The ability owner
     */
    public ExampleAbility1(@NotNull IAbilityUser user) {
        super(user, "Example Ability 1", "My First Ability!");
    }

    /**
     * Let's bring our ability to life!
     * {@link ActiveAbility#getEntity()} is an interface method that returns the current user's({@link ActiveAbility#getUser()}) entity
     * We can use that to perform various actions regarding it

     * In this example, when the key is pressed, we will message our entity "Key Down!", and when the key is released we will message our entity "Key Up!"
     * Simple, right?
     * @param isKeyDown Is the key pressed(true) or released(false)
     */
    @Override
    public void execute(boolean isKeyDown) {
        if (isKeyDown)
            getEntity().sendMessage(Text.literal("Key Down!"));
        else
            getEntity().sendMessage(Text.literal("Key Up!"));
    }

}
