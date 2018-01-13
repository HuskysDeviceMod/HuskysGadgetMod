package net.thegaminghuskymc.gadgetmod.programs.system.layout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.thegaminghuskymc.gadgetmod.api.ApplicationManager;
import net.thegaminghuskymc.gadgetmod.api.app.Application;
import net.thegaminghuskymc.gadgetmod.api.app.component.Image;
import net.thegaminghuskymc.gadgetmod.api.app.emojie_packs.Icons;
import net.thegaminghuskymc.gadgetmod.api.app.Layout;
import net.thegaminghuskymc.gadgetmod.api.app.component.Button;
import net.thegaminghuskymc.gadgetmod.api.app.component.Label;
import net.thegaminghuskymc.gadgetmod.core.Laptop;
import net.thegaminghuskymc.gadgetmod.util.RenderHelper;

import javax.annotation.Nullable;
import java.awt.*;

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

        Image imageBanner = new Image(0, 0, 270, 44, "https://i.imgur.com/VAGCpKY.jpg");
        this.addComponent(imageBanner);

        Label labelBanner = new Label(RenderHelper.unlocaliseName(ApplicationManager.getApplication("hgm:app_store").getName()), 10, 36);
        labelBanner.setScale(2);
        this.addComponent(labelBanner);

    }

    @Override
    public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {

        Color color = new Color(Laptop.getSystem().getSettings().getColourScheme().getHeaderColour());
        Gui.drawRect(x, y + 44, x + width, y + 45, color.brighter().getRGB());
        Gui.drawRect(x, y + 45, x + width, y + 60, color.getRGB());
        Gui.drawRect(x, y + 60, x + width, y + 61, color.darker().getRGB());

        super.render(laptop, mc, x, y, mouseX, mouseY, windowActive, partialTicks);
    }

}