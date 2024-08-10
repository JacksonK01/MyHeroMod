package net.michaeljackson23.mineademia.abilitysystem.intr.ability.active;

import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * Base class of all segmented abilities
 */
public interface ISegmentedAbility extends IActiveAbility, ICooldownAbility {

    int getMaxCharges();

    int getCharges();
    void setCharges(int charges);

    void onExecuteCharge(int charge);

    @Override
    default void execute(boolean isKeyDown) {
        int charges = getCharges();

        if (charges > 0) {
            setCharges(charges - 1);
            onExecuteCharge(charges - 1);
        }
    }

    default void onCooldownOver() {
        setCharges(getCharges() + 1);
    }

    @Override
    default void encode(@NotNull PacketByteBuf buffer) {
        ICooldownAbility.super.encode(buffer);

        buffer.writeInt(getMaxCharges());
        buffer.writeInt(getCharges());
    }

}
