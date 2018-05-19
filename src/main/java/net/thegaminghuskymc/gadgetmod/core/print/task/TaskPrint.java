package net.thegaminghuskymc.gadgetmod.core.print.task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.thegaminghuskymc.gadgetmod.api.print.IPrint;
import net.thegaminghuskymc.gadgetmod.api.task.Task;
import net.thegaminghuskymc.gadgetmod.core.network.NetworkDevice;
import net.thegaminghuskymc.gadgetmod.core.network.Router;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityNetworkDevice;
import net.thegaminghuskymc.gadgetmod.tileentity.TileEntityPrinter;

import java.util.UUID;

public class TaskPrint extends Task {
    private BlockPos devicePos;
    private UUID printerId;
    private IPrint print;

    private TaskPrint() {
        super("print");
    }

    public TaskPrint(BlockPos devicePos, NetworkDevice printer, IPrint print) {
        this();
        this.devicePos = devicePos;
        this.printerId = printer.getId();
        this.print = print;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt) {
        nbt.setLong("devicePos", devicePos.toLong());
        nbt.setUniqueId("printerId", printerId);
        nbt.setTag("print", IPrint.writeToTag(print));
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player) {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
        if (tileEntity instanceof TileEntityNetworkDevice) {
            TileEntityNetworkDevice device = (TileEntityNetworkDevice) tileEntity;
            Router router = device.getRouter();
            if (router != null) {
                TileEntityNetworkDevice printer = router.getDevice(world, nbt.getUniqueId("printerId"));
                if (printer != null && printer instanceof TileEntityPrinter) {
                    IPrint print = IPrint.loadFromTag(nbt.getCompoundTag("print"));
                    ((TileEntityPrinter) printer).addToQueue(print);
                    this.setSuccessful();
                }
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt) {

    }

    @Override
    public void processResponse(NBTTagCompound nbt) {

    }
}
