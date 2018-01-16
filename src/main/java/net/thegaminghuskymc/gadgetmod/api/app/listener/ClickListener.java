package net.thegaminghuskymc.gadgetmod.api.app.listener;

/**
 * The click listener interface. Used for handling clicks
 * on components.
 *
 */
public interface ClickListener {
    /**
     * Called when component is clicked
     *
     * @param mouseButton the mouse button used to click
     */
    void onClick(int mouseX, int mouseY, int mouseButton);
}
