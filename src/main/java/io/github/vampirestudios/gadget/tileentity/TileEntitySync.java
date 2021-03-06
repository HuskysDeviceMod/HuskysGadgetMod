package io.github.vampirestudios.gadget.tileentity;

import net.hdt.huskylib2.block.tile.TileMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import io.github.vampirestudios.gadget.util.TileEntityUtil;

public abstract class TileEntitySync extends TileMod {

    protected NBTTagCompound pipeline = new NBTTagCompound();

    public void sync() {
        TileEntityUtil.markBlockForUpdate(world, pos);
        markDirty();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public final NBTTagCompound getUpdateTag() {
        if (!pipeline.isEmpty()) {
            NBTTagCompound updateTag = super.writeToNBT(pipeline);
            pipeline = new NBTTagCompound();
            return updateTag;
        }
        return super.writeToNBT(writeSyncTag());
    }

    public abstract NBTTagCompound writeSyncTag();

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    public NBTTagCompound getPipeline() {
        return pipeline;
    }
}