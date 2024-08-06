package net.michaeljackson23.mineademia.client.gui.quirktablet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class MockQuirkTabletGui extends Screen {

    public static final int MAX_X = 2;
    public static final int SPACING_Y = 10;


    public MockQuirkTabletGui(Text title) {
        super(title);
    }

    private ButtonWidget none;
    private ButtonWidget hchh_ice;
    private ButtonWidget explosion;
    private ButtonWidget whirlwind;

    @Override
    protected void init() {
//        none = ButtonWidget.builder(Text.literal("none"), button -> {
//                    PacketByteBuf data = PacketByteBufs.create();
//                    data.writeString("");
//                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
//                })
//                .dimensions(width / 2 - 205, 20, 200, 20)
//                .tooltip(Tooltip.of(Text.literal("changes quirk")))
//                .build();
//
//        hchh_ice = ButtonWidget.builder(Text.literal("HCHH ICE"), button -> {
//                    PacketByteBuf data = PacketByteBufs.create();
//                    data.writeString("hchh_cold");
//                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
//                })
//                .dimensions(width / 2 + 5, 20, 200, 20)
//                .tooltip(Tooltip.of(Text.literal("changes quirk")))
//                .build();

//        explosion = ButtonWidget.builder(Text.literal("Explosion"), button -> {
//                    PacketByteBuf data = PacketByteBufs.create();
//                    data.writeString("explosion");
//                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
//                })
//                .dimensions(width / 2 - 205, 40, 200, 20)
//                .tooltip(Tooltip.of(Text.literal("changes quirk")))
//                .build();

        none = createButton("Quirkless", "", 0);
        hchh_ice = createButton("Half Cold Half Hot - Ice", "hchh_cold", 1);
        explosion = createButton("Explosion", "explosion", 2);
        whirlwind = createButton("Whirlwind", "whirlwind", 3);

        addDrawableChild(none);
        addDrawableChild(hchh_ice);
        addDrawableChild(explosion);
        addDrawableChild(whirlwind);
    }

    private ButtonWidget createButton(String name, String value, int index) {
        int x = index % MAX_X;
        int y = index / MAX_X;

        int xCoord = width / 2 + (x == 1 ? 5 : -205);
        int yCoord = 20 + (20 + SPACING_Y) * y;

        return ButtonWidget.builder(Text.literal(name), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString(value);
                    ClientPlayNetworking.send(Networking.MOCK_CHANGE_QUIRK_WITH_TABLET, data);
                })
                .dimensions(xCoord, yCoord, 200, 20)
                .tooltip(Tooltip.of(Text.literal("changes quirk")))
                .build();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
