package net.michaeljackson23.mineademia.gui.vestige;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
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

    // Button dimensions
    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 20;

    // These variables need to be initialized in the constructor because they use the screen dimensions
    private final ButtonWidget blackwhip;

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
    }

    @Override
    protected void init() {
        // Adjust button position to account for the position of the GUI window
        int guiLeft = (this.width - BACKGROUND_WIDTH) / 2;
        int guiTop = (this.height - BACKGROUND_HEIGHT) / 2;

        blackwhip.setX(guiLeft + (BACKGROUND_WIDTH - BUTTON_WIDTH) / 2);
        blackwhip.setY(guiTop + (BACKGROUND_HEIGHT - BUTTON_HEIGHT) / 2);

        addDrawableChild(blackwhip);
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
