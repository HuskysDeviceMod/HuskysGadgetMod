package net.thegaminghuskymc.gadgetmod.programs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.thegaminghuskymc.gadgetmod.api.app.Application;
import net.thegaminghuskymc.gadgetmod.api.app.Layout;
import net.thegaminghuskymc.gadgetmod.api.app.component.Button;
import net.thegaminghuskymc.gadgetmod.api.app.component.ItemList;
import net.thegaminghuskymc.gadgetmod.api.app.component.TextField;
import net.thegaminghuskymc.gadgetmod.api.app.emojie_packs.Icons;
import net.thegaminghuskymc.gadgetmod.api.app.renderer.ListItemRenderer;
import net.thegaminghuskymc.gadgetmod.core.BaseDevice;

import javax.annotation.Nullable;
import java.awt.*;

public class ApplicationPixelBrowser extends Application {

    public ApplicationPixelBrowser() {
        this.setDefaultWidth(270);
        this.setDefaultHeight(140);
    }

    @Override
    public void init(@Nullable NBTTagCompound intent) {
        TextField textField = new TextField(5, 5, 200);
        textField.setPlaceholder("URL");
        super.addComponent(textField);

        Button user = new Button(210, 5, Icons.USER);
        super.addComponent(user);

        Button dropDown = new Button(240, 5, Icons.DOTS_HORIZONTAL);
        dropDown.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (BaseDevice.getSystem().hasContext()) {
                BaseDevice.getSystem().closeContext();
            } else {
                BaseDevice.getSystem().openContext(createDropdown(), mouseX - 100, mouseY + 5);
            }
        });
        super.addComponent(dropDown);

    }

    @Override
    public void load(NBTTagCompound tagCompound) {

    }

    @Override
    public void save(NBTTagCompound tagCompound) {

    }

    private static Layout createDropdown() {

        Layout layout = new Layout.Context(100, 100);
        layout.yPosition = 70;
        layout.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
                Gui.drawRect(x, y, x + width, y + height, new Color(0.65F, 0.65F, 0.65F, 0.9F).getRGB()));

        ItemList<Button> itemListButtons = new ItemList<>(5, 5, 90, 4);
        itemListButtons.setListItemRenderer(new ListItemRenderer<Button>(16) {
            @Override
            public void render(Button button, Gui gui, Minecraft mc, int x, int y, int width, int height, boolean selected) {
                Gui.drawRect(x, y, x + width, y + height, selected ? Color.DARK_GRAY.getRGB() : Color.GRAY.getRGB());
                gui.drawString(mc.fontRenderer, "Router", x + 16, y + 4, Color.WHITE.getRGB());
            }
        });
        layout.addComponent(itemListButtons);

        return layout;
    }

}
