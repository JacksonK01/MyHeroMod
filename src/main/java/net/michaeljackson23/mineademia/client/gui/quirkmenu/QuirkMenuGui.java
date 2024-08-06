package net.michaeljackson23.mineademia.client.gui.quirkmenu;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class QuirkMenuGui extends Screen implements ParentElement {
    private final static Identifier BACKGROUND_TEXTURE = new Identifier(Mineademia.MOD_ID, "textures/gui/quirkmenu/hero_card.png");
    //Vanilla texture
    private static final Identifier SCROLLER_TEXTURE = new Identifier("minecraft", "textures/gui/sprites/container/creative_inventory/scroller.png");

    private final int BACKGROUND_WIDTH = 240;
    private final int BACKGROUND_HEIGHT = 170;

    private final int SCROLLER_WIDTH = 15;
    private final int SCROLLER_HEIGHT = 25;

    private float scrollPosition = 0f;
    private boolean scrolling = false;

    public QuirkMenuGui(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        int guiLeft = (this.width - BACKGROUND_WIDTH) / 2;
        int guiTop = (this.height - BACKGROUND_HEIGHT) / 2;

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
        context.drawTexture(SCROLLER_TEXTURE, x + 10, y - 10, 0, 0, SCROLLER_WIDTH, SCROLLER_HEIGHT, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

}
