package net.michaeljackson23.mineademia.gui.quirktablet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.networking.Client2Server;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class QuirkTabletGui extends Screen {

    public QuirkTabletGui(Text title) {
        super(title);
    }
    private ButtonWidget ofa;
    private ButtonWidget explosion;
    private ButtonWidget hchh;
    private ButtonWidget whirlwind;
    private ButtonWidget elect;

    @Override
    protected void init() {
        ofa = ButtonWidget.builder(Text.literal("One for all"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("One For All");
                    ClientPlayNetworking.send(Client2Server.QUIRKTABLETGUIOPEN, data);
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();
        explosion = ButtonWidget.builder(Text.literal("Explosion"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Explosion");
                    ClientPlayNetworking.send(Client2Server.QUIRKTABLETGUIOPEN, data);
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();

        hchh = ButtonWidget.builder(Text.literal("Hchh"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Half-Cold Half-Hot");
                    ClientPlayNetworking.send(Client2Server.QUIRKTABLETGUIOPEN, data);
                })
                .dimensions(width / 2 - 205, 40, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();

        whirlwind = ButtonWidget.builder(Text.literal("Whirlwind"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Whirlwind");
                    ClientPlayNetworking.send(Client2Server.QUIRKTABLETGUIOPEN, data);
                })
                .dimensions(width / 2 + 5, 40, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();
        elect = ButtonWidget.builder(Text.literal("Electrification"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Electrification");
                    ClientPlayNetworking.send(Client2Server.QUIRKTABLETGUIOPEN, data);
                })
                .dimensions(width / 2 - 205, 60, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();

        addDrawableChild(ofa);
        addDrawableChild(explosion);
        addDrawableChild(hchh);
        addDrawableChild(whirlwind);
        addDrawableChild(elect);
    }

    public boolean shouldPause() {
        return false;
    }
}
