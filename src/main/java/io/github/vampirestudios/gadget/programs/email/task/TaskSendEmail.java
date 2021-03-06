package io.github.vampirestudios.gadget.programs.email.task;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import io.github.vampirestudios.gadget.api.task.Task;
import io.github.vampirestudios.gadget.programs.email.EmailManager;
import io.github.vampirestudios.gadget.programs.email.object.Email;

public class TaskSendEmail extends Task {

    private Email email;
    private String to;

    public TaskSendEmail() {
        super("send_email");
    }

    public TaskSendEmail(Email email, String to) {
        this();
        this.email = email;
        this.to = to;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt) {
        this.email.writeToNBT(nbt);
        nbt.setString("to", this.to);
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player) {
        String name = EmailManager.INSTANCE.getName(player);
        if (name != null) {
            Email email = Email.readFromNBT(nbt);
            email.setAuthor(name);
            if (EmailManager.INSTANCE.addEmailToInbox(email, nbt.getString("to"))) {
                this.setSuccessful();
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
