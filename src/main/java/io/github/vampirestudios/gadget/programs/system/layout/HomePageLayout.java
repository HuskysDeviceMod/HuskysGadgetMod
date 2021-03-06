package io.github.vampirestudios.gadget.programs.system.layout;

import io.github.vampirestudios.gadget.api.ApplicationManager;
import io.github.vampirestudios.gadget.api.app.Application;
import io.github.vampirestudios.gadget.api.app.Layout;
import io.github.vampirestudios.gadget.api.app.component.Button;
import io.github.vampirestudios.gadget.api.app.component.Image;
import io.github.vampirestudios.gadget.api.app.component.Label;
import io.github.vampirestudios.gadget.api.app.emojie_packs.Icons;
import io.github.vampirestudios.gadget.core.BaseDevice;
import io.github.vampirestudios.gadget.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;

import static io.github.vampirestudios.gadget.Reference.MOD_ID;

public class HomePageLayout extends Layout {
    protected Application app;
    private Layout previous;

    public HomePageLayout(int width, int height, Application app, @Nullable Layout previous) {
        super(width, height);
        this.app = app;
        this.previous = previous;
    }

    @Override
    public void init() {
        if (previous != null) {
            Button btnBack = new Button(2, 2, Icons.ARROW_LEFT);
            btnBack.setClickListener((mouseX, mouseY, mouseButton) ->
            {
                if (mouseButton == 0) {
                    app.setCurrentLayout(previous);
                }
            });
            this.addComponent(btnBack);
        }

        Image imageBanner = new Image(0, 0, previous.width, 60);
        imageBanner.setImage(new ResourceLocation(MOD_ID, "textures/gui/app_market_background.png"));
        imageBanner.setDrawFull(true);
        this.addComponent(imageBanner);

        Label labelBanner = new Label(RenderHelper.unlocaliseName(ApplicationManager.getApplication("hgm:app_store").getName()), 10, 36);
        labelBanner.setScale(2);
        this.addComponent(labelBanner);

    }

    @Override
    public void render(BaseDevice laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {

        Color color = new Color(BaseDevice.getSystem().getSettings().getColourScheme().getHeaderColour());
        Gui.drawRect(x, y + 44, x + width, y + 45, color.brighter().getRGB());
        Gui.drawRect(x, y + 45, x + width, y + 60, color.getRGB());
        Gui.drawRect(x, y + 60, x + width, y + 61, color.darker().getRGB());

        super.render(laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
    }

}