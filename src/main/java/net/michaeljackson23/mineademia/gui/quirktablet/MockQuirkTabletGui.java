package net.michaeljackson23.mineademia.gui.quirktablet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class MockQuirkTabletGui extends Screen {

    public MockQuirkTabletGui(Text title) {
        super(title);
    }

    private ButtonWidget none;
    private ButtonWidget hchh_ice;

    @Override
    protected void init() {
        none = ButtonWidget.builder(Text.literal("none"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("");
                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();
        hchh_ice = ButtonWidget.builder(Text.literal("HCHH ICE"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("hchh_cold");
                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();

        addDrawableChild(none);
        addDrawableChild(hchh_ice);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
