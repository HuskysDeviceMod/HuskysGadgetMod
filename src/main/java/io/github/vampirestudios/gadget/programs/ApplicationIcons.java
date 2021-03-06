package io.github.vampirestudios.gadget.programs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import io.github.vampirestudios.gadget.api.app.Application;
import io.github.vampirestudios.gadget.api.app.IIcon;
import io.github.vampirestudios.gadget.api.app.Layout;
import io.github.vampirestudios.gadget.api.app.annontation.DeviceApplication;
import io.github.vampirestudios.gadget.api.app.component.Button;
import io.github.vampirestudios.gadget.api.app.component.ComboBox;
import io.github.vampirestudios.gadget.api.app.emojie_packs.*;
import io.github.vampirestudios.gadget.programs.system.layout.StandardLayout;

import javax.annotation.Nullable;

import static io.github.vampirestudios.gadget.Reference.MOD_ID;

@DeviceApplication(modId = MOD_ID, appId = "icons")
public class ApplicationIcons extends Application {
    private int offset;

    private StandardLayout layoutMain;
    private Layout layoutContainer;
    private ComboBox.List<IconSet> iconSetComboBox;

    public ApplicationIcons() {
        this.setDefaultWidth(332);
        this.setDefaultHeight(150);
    }

    @Override
    public void init(@Nullable NBTTagCompound intent) {
        layoutMain = new StandardLayout(TextFormatting.BOLD + "Icons", 330, 153, this, null);
        layoutMain.setIcon(Icons.HOME);

        layoutContainer = new Layout(330, 153);
        layoutMain.addComponent(layoutContainer);

        IconSet[] iconSets = new IconSet[]{
                new IconSet("Standard Icons", Icons.values()),
                new IconSet("Alphabet", Alphabet.values()),
                new IconSet("Emojies", EmojiesMRC.values()),
                new IconSet("Faces", Faces.values()),
                new IconSet("Food", Food.values()),
                new IconSet("Logos", Logos.values()),
                new IconSet("Other Emojies", OtherEmojis.values())
        };
        iconSetComboBox = new ComboBox.List<>(191, 3, 100, iconSets);
        iconSetComboBox.setChangeListener((oldValue, newValue) ->
        {
            offset = 0;
            updateIcons();
        });
        layoutMain.addComponent(iconSetComboBox);

        Button btnPrevPage = new Button(297, 3, Icons.ARROW_LEFT);
        btnPrevPage.setToolTip("Previous Page", "Go to previous page of this icon set");
        btnPrevPage.setSize(14, 14);
        btnPrevPage.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (offset > 0) offset--;
                updateIcons();
            }
        });
        layoutMain.addComponent(btnPrevPage);

        Button btnNextPage = new Button(313, 3, Icons.ARROW_RIGHT);
        btnNextPage.setToolTip("Next Page", "Go to next page of this icon set");
        btnNextPage.setSize(14, 14);
        btnNextPage.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (offset < (iconSetComboBox.getSelectedItem().getIcons().length / 126)) offset++;
                updateIcons();
            }
        });
        layoutMain.addComponent(btnNextPage);

        this.updateIcons();
        this.setCurrentLayout(layoutMain);

        this.setResizable(true);
    }

    private void updateIcons() {
        layoutContainer.clear();
        IconSet set = iconSetComboBox.getSelectedItem();
        for (int i = 0; i < 126 && i < set.getIcons().length - (offset * 126); i++) {
            Enum<? extends IIcon> anEnum = set.getIcons()[i + (offset * 126)];
            IIcon icon = (IIcon) anEnum;
            int posX = (i % 18) * 18 - 1;
            int posY = (i / 18) * 18 + 20;
            Button button = new Button(5 + posX, 5 + posY, icon);
            button.setToolTip("Icon", anEnum.name());
            layoutContainer.addComponent(button);
        }
        layoutContainer.updateComponents(layoutContainer.xPosition, layoutContainer.yPosition);
    }

    @Override
    public void onClose() {
        super.onClose();
        offset = 0;
        layoutMain = null;
        layoutContainer = null;
        iconSetComboBox = null;
    }

    @Override
    public void load(NBTTagCompound tagCompound) {

    }

    @Override
    public void save(NBTTagCompound tagCompound) {

    }

    public static class IconSet {
        private String name;
        private Enum<? extends IIcon>[] icons;

        public IconSet(String name, Enum<? extends IIcon>[] icons) {
            this.name = name;
            this.icons = icons;
        }

        public String getName() {
            return name;
        }

        public Enum<? extends IIcon>[] getIcons() {
            return icons;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}