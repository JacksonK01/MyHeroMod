package net.michaeljackson23.mineademia.animations;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class AnimationRegister {
    //Possible settings for animations
    //testAnimation.addModifierBefore(new SpeedModifier(1f)); //This will be slow
    //testAnimation.addModifierBefore(new MirrorModifier(true)); //Mirror the animation
    //animationStack.addAnimLayer(42, testAnimation); //Add and save the animation container for later use.
    public static void register() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "reset"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "griddy"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "fireinferno"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
    }
}
