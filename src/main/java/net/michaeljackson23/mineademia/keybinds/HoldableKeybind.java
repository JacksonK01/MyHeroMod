package net.michaeljackson23.mineademia.keybinds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HoldableKeybind extends KeyBinding {
    private boolean sentPacket = false;

    public HoldableKeybind(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
    }

    public void holdAndReleaseAction(HoldableKeybind this, Identifier Packet) {
        if (this.isPressed()) {
            if (!this.hasSentPacket()) {
                PacketByteBuf data = PacketByteBufs.create();
                data.writeBoolean(true);
                ClientPlayNetworking.send(Packet, data);
            }
            this.setSentPacket(true);
        } else if (this.hasSentPacket()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeBoolean(false);
            ClientPlayNetworking.send(Packet, data);
            this.setSentPacket(false);
        }
    }

    public boolean hasSentPacket() {
        return this.sentPacket;
    }

    public void setSentPacket(boolean state) {
        this.sentPacket = state;
    }
}
