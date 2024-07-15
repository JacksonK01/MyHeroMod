package net.michaeljackson23.mineademia.animations;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.michaeljackson23.mineademia.Mineademia;
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
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "dodge"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "griddy"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "slide"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "from_slide_to_flipkick"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "air_kick_down"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "explode_flip"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "ap_shot"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "st_louis_smash_right_leg"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "st_louis_smash_left_leg"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "manchester_smash_shoot_style"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "charge_up_leap"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "shoot_style_kick_on_ground"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "shoot_style_kick_in_air"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_punch_1"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_punch_2"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_punch_3"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_kick_1"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_kick_2"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_kick_3"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_aerial_ground_1"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "combo_aerial_air_1"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "whirlwind_up_down"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new Identifier(Mineademia.MOD_ID, "wind_ballista"), 0, (player) -> {
            return player instanceof ClientPlayerEntity ? new ModifierLayer<>() : null;
        });
    }

//    private static ModifierLayer generateModifierLayer() {
//        ModifierLayer modifierLayer = new ModifierLayer<>();
//        modifierLayer.addModifier(AbstractFadeModifier.);
//        FirstPersonConfiguration configuration =
//        return modifierLayer;
//    }
}
