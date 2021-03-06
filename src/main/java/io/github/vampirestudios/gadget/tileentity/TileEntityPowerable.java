package io.github.vampirestudios.gadget.tileentity;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import io.github.vampirestudios.gadget.util.IColored;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileEntityPowerable extends TileEntitySync implements ITickable, IColored {

    private EnumDyeColor color = EnumDyeColor.WHITE;
    private UUID deviceId;
    private String name;

    public final UUID getId() {
        if (deviceId == null) {
            deviceId = UUID.randomUUID();
        }
        return deviceId;
    }

    public abstract String getDeviceName();

    public String getCustomName() {
        return hasCustomName() ? name : getDeviceName();
    }

    public void setCustomName(String name) {
        this.name = name;
    }

    public boolean hasCustomName() {
        return !StringUtils.isEmpty(name);
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getCustomName());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("deviceId", getId().toString());
        if (hasCustomName()) {
            compound.setString("name", name);
        }
        compound.setByte("color", (byte) color.getMetadata());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("deviceId", Constants.NBT.TAG_STRING)) {
            deviceId = UUID.fromString(compound.getString("deviceId"));
        }
        if (compound.hasKey("name", Constants.NBT.TAG_STRING)) {
            name = compound.getString("name");
        }
        if (compound.hasKey("color", Constants.NBT.TAG_BYTE)) {
            this.color = EnumDyeColor.byMetadata(compound.getByte("color"));
        }
    }

    @Override
    public NBTTagCompound writeSyncTag() {
        NBTTagCompound tag = new NBTTagCompound();
        if (hasCustomName()) {
            tag.setString("name", name);
        }
        tag.setByte("color", (byte) color.getMetadata());
        return tag;
    }

    public final EnumDyeColor getColor() {
        return color;
    }

    public final void setColor(EnumDyeColor color) {
        this.color = color;
    }
}