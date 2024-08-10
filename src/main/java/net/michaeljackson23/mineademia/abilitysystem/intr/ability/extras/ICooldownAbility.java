package net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras;

import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * If an ability needs a cooldown, it'll need to implement this
 */
public interface ICooldownAbility extends IActiveAbility {

    @NotNull
    Cooldown getCooldown();

    default void resetCooldown() {
        getCooldown().reset();
    }

    default boolean isCooldownReady() {
        return getCooldown().isReady();
    }

    default boolean isCooldownReadyAndReset() {
        return getCooldown().isReadyAndReset();
    }

    default boolean shouldCooldownDisplayOnHud() {
        return true;
    }

    @Override
    default void encode(@NotNull PacketByteBuf buffer) {
        IActiveAbility.super.encode(buffer);

        Cooldown cooldown = getCooldown();
        buffer.writeInt(cooldown.getCooldownTicks());
        buffer.writeInt(cooldown.getTicksRemaining());
    }

}
