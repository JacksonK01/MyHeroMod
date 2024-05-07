package net.michaeljackson23.mineademia.animations;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;

public interface IAnimationHandler {
    ModifierLayer<IAnimation> modid_getModAnimation();
}
