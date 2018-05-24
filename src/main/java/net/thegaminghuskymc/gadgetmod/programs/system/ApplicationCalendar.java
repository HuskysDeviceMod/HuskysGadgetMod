package net.thegaminghuskymc.gadgetmod.programs.system;

import net.minecraft.nbt.NBTTagCompound;
import net.thegaminghuskymc.gadgetmod.api.app.Application;
import net.thegaminghuskymc.gadgetmod.api.app.annontation.DeviceApplication;
import net.thegaminghuskymc.gadgetmod.api.app.component.Label;

import javax.annotation.Nullable;

import static net.thegaminghuskymc.gadgetmod.Reference.MOD_ID;

@DeviceApplication(modId = MOD_ID, appId = "calendar")
public class ApplicationCalendar extends Application {

    @Override
    public void init(@Nullable NBTTagCompound intent) {
        Label test = new Label("Test", 50, 50);
        super.addComponent(test);
    }

    @Override
    public void load(NBTTagCompound tagCompound) {

    }

    @Override
    public void save(NBTTagCompound tagCompound) {

    }
}
