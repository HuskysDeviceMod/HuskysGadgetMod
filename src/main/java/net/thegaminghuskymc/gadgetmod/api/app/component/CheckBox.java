package net.thegaminghuskymc.gadgetmod.api.app.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.thegaminghuskymc.gadgetmod.api.app.Component;
import net.thegaminghuskymc.gadgetmod.api.app.listener.ClickListener;
import net.thegaminghuskymc.gadgetmod.core.Laptop;
import net.thegaminghuskymc.gadgetmod.util.GuiHelper;

import java.awt.*;


public class CheckBox extends Component implements RadioGroup.Item {

    protected String name;
    private boolean checked = false;
    protected RadioGroup group = null;

    protected ClickListener listener = null;

    private int textColour = Color.WHITE.getRGB();
    private int backgroundColour = Color.GRAY.getRGB();
    private int borderColour = Color.BLACK.getRGB();
    private int checkedColour = Color.DARK_GRAY.getRGB();

    /**
     * Default check box constructor
     *
     * @param name the name of the check box
     * @param left how many pixels from the left
     * @param top  how many pixels from the top
     */
    public CheckBox(String name, int left, int top) {
        super(left, top);
        this.name = name;
    }

    /**
     * Sets the radio group for this button.
     *
     * @param group the radio group.
     */
    public void setRadioGroup(RadioGroup group) {
        this.group = group;
        this.group.add(this);
    }

    /**
     * Sets the click listener. Use this to handle custom actions
     * when you press the check box.
     *
     * @param listener the click listener
     */
    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean windowActive, float partialTicks) {
        if (this.visible) {
            if (group == null) {
                drawRect(xPosition, yPosition, xPosition + 10, yPosition + 10, borderColour);
                drawRect(xPosition + 1, yPosition + 1, xPosition + 9, yPosition + 9, backgroundColour);
                if (checked) {
                    drawRect(xPosition + 2, yPosition + 2, xPosition + 8, yPosition + 8, checkedColour);
                }
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(COMPONENTS_GUI);
                drawTexturedModalRect(xPosition, yPosition, checked ? 10 : 0, 60, 10, 10);
            }
            drawString(mc.fontRenderer, name, xPosition + 12, yPosition + 1, textColour);
        }
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (!this.visible || !this.enabled)
            return;

        if (GuiHelper.isMouseInside(mouseX, mouseY, xPosition, yPosition, xPosition + 10, yPosition + 10)) {
            if (group != null) {
                group.deselect();
            }
            this.checked = !checked;
            if (listener != null) {
                listener.onClick(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public boolean isSelected() {
        return checked;
    }

    @Override
    public void setSelected(boolean enabled) {
        this.checked = enabled;
    }

}
