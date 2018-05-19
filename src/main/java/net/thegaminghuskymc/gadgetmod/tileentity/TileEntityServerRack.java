package net.thegaminghuskymc.gadgetmod.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.thegaminghuskymc.huskylib2.blocks.tile.TileMod;

public class TileEntityServerRack extends TileMod implements ITickable {

    @SideOnly(Side.CLIENT)
    public float rotation;
    private boolean hasServers = false, hasConnectedPower = false;

    @Override
    public void update() {
        if (world.isRemote) {
            if (rotation > 0) {
                rotation -= 10F;
            } else if (rotation < 110) {
                rotation += 10F;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (compound.hasKey("hasServers")) {
            this.hasServers = compound.getBoolean("hasServers");
        }
        if (compound.hasKey("hasConnectedPower")) {
            this.hasConnectedPower = compound.getBoolean("hasConnectedPower");
        }
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        compound.setBoolean("hasServers", hasServers);
        compound.setBoolean("hasConnectedPower", hasConnectedPower);
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 16384;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public boolean hasConnectedPower() {
        return hasConnectedPower;
    }

    public void connectUnconnectPower() {
        hasConnectedPower = !hasConnectedPower;
    }

    public boolean hasServers() {
        return hasServers;
    }
}