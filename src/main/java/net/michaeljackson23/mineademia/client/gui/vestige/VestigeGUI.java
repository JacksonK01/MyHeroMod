package net.michaeljackson23.mineademia.client.gui.vestige;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VestigeGUI extends Screen {
    private final static Identifier BACKGROUND_TEXTURE = new Identifier(Mineademia.MOD_ID, "textures/gui/vestige/vestige_select.png");
    private final int BACKGROUND_WIDTH = 192;
    private final int BACKGROUND_HEIGHT = 149;

    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 20;

    private final ButtonWidget blackwhip;
    private final ButtonWidget smokescreen;
    private final ButtonWidget faJin;
    private final ButtonWidget floatB;
    private final ButtonWidget gearShift;

    public VestigeGUI(Text title) {
        super(title);
        // Center the button within the GUI
        int buttonX = (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2;
        int buttonY = (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2;
        blackwhip = ButtonWidget.builder(Text.literal("Blackwhip"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Blackwhip");
                    ClientPlayNetworking.send(Networking.SELECT_VESTIGE_GUI, data);
                    close();
                })
                .dimensions(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("Select Blackwhip as your vestige quirk")))
                .build();
        smokescreen = ButtonWidget.builder(Text.literal("Smokescreen"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Smokescreen");
                    ClientPlayNetworking.send(Networking.SELECT_VESTIGE_GUI, data);
                    close();
                })
                .dimensions(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("Select Smokescreen as your vestige quirk")))
                .build();
        faJin = ButtonWidget.builder(Text.literal("FaJin"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("FaJin");
                    ClientPlayNetworking.send(Networking.SELECT_VESTIGE_GUI, data);
                    close();
                })
                .dimensions(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("Select FaJin as your vestige quirk")))
                .build();
        floatB = ButtonWidget.builder(Text.literal("Float"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Float");
                    ClientPlayNetworking.send(Networking.SELECT_VESTIGE_GUI, data);
                    close();
                })
                .dimensions(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("Select Float as your vestige quirk")))
                .build();
        gearShift = ButtonWidget.builder(Text.literal("Gearshift"), button -> {
                    PacketByteBuf data = PacketByteBufs.create();
                    data.writeString("Gearshift");
                    ClientPlayNetworking.send(Networking.SELECT_VESTIGE_GUI, data);
                    close();
                })
                .dimensions(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .tooltip(Tooltip.of(Text.literal("Select Gearshift as your vestige quirk")))
                .build();
    }

    @Override
    protected void init() {
        int guiLeft = (this.width - BACKGROUND_WIDTH) / 2;
        int guiTop = (this.height - BACKGROUND_HEIGHT) / 2;

        blackwhip.setX(guiLeft + (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2);
        blackwhip.setY(guiTop + (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2);

        smokescreen.setX(guiLeft + (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2);
        smokescreen.setY((guiTop + (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2) + BUTTON_HEIGHT);

        floatB.setX(guiLeft + (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2);
        floatB.setY((guiTop + (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2) + (BUTTON_HEIGHT * 2));

        faJin.setX(guiLeft + (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2);
        faJin.setY((guiTop + (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2) + (BUTTON_HEIGHT * 3));

        addDrawableChild(blackwhip);
        addDrawableChild(smokescreen);
        addDrawableChild(faJin);
        addDrawableChild(floatB);
//        addDrawableChild(gearShift);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        int i = (this.width - BACKGROUND_WIDTH) / 2;
        int j = (this.height - BACKGROUND_HEIGHT) / 2;
        drawWindow(context, i, j);
        super.render(context, mouseX, mouseY, delta);
    }

    public void drawWindow(DrawContext context, int x, int y) {
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }
}
