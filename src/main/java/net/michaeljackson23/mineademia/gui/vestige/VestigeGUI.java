package net.michaeljackson23.mineademia.gui.vestige;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VestigeGUI extends Screen {
    private final static Identifier BACKGROUND_TEXTURE = new Identifier(Mineademia.MOD_ID, "textures/gui/vestige/vestige_select.png");
    private final int BACKGROUND_WIDTH = 192;
    private final int BACKGROUND_HEIGHT = 149;

    public VestigeGUI(Text title) {
        super(title);
    }

    protected void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = (this.width - BACKGROUND_WIDTH) / 2;
        int j = (this.height - BACKGROUND_HEIGHT) / 2;
        drawWindow(context, i, j);
    }

    public void drawWindow(DrawContext context, int x, int y) {
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

}
