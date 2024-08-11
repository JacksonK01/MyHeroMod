package net.michaeljackson23.mineademia.client.gui.quirktablet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class MockQuirkTabletGui extends Screen {

    public static final int MAX_X = 2;
    public static final int SPACING_Y = 5;


    private int index;

    public MockQuirkTabletGui(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        this.index = 0;

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ButtonWidget example = createButton("Example", "example");
            addDrawableChild(example);
        }

        ButtonWidget none = createButton("Quirkless", "");
        ButtonWidget hchh_cold = createButton("Half Cold Half Hot - Cold", "hchh_cold");
        ButtonWidget explosion = createButton("Explosion", "explosion");
        ButtonWidget whirlwind = createButton("Whirlwind", "whirlwind");
        ButtonWidget ofa = createButton("One For All", "ofa");
        ButtonWidget engine = createButton("Engine", "engine");

        addDrawableChild(none);
        addDrawableChild(hchh_cold);
        addDrawableChild(explosion);
        addDrawableChild(whirlwind);
        addDrawableChild(ofa);
        addDrawableChild(engine);
    }

    private ButtonWidget createButton(String name, String value) {
        int x = index % MAX_X;
        int y = index / MAX_X;

        int xCoord = width / 2 + (x == 1 ? 5 : -205);
        int yCoord = 20 + (20 + SPACING_Y) * y;

        index++;

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