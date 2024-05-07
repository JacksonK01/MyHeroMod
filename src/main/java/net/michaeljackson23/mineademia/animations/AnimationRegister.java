package net.michaeljackson23.mineademia.animations;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Random;

public class AnimationRegister {
    public static void register() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.Mod_id, "griddy"), 0, (player) -> {
            if (player instanceof ClientPlayerEntity) {
                //animationStack.addAnimLayer(42, testAnimation); //Add and save the animation container for later use.
                ModifierLayer<IAnimation> testAnimation =  new ModifierLayer<>();

                testAnimation.addModifierBefore(new SpeedModifier(1f)); //This will be slow
//                testAnimation.addModifierBefore(new MirrorModifier(true)); //Mirror the animation
                return testAnimation;
            }
            return null;
        });
    }
    public static void playAnimation() {
        ModifierLayer<IAnimation> testAnimation;

        testAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(MinecraftClient.getInstance().player).get(new Identifier(Mineademia.Mod_id, "griddy"));
        testAnimation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(100, (modelName, type, value) -> value),
                new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new Identifier(Mineademia.Mod_id, "griddy")))
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(false))
        );
//        testAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.LINEAR), null);
    }
}
