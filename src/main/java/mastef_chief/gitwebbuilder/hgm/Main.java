package mastef_chief.gitwebbuilder.hgm;

import mastef_chief.gitwebbuilder.hgm.app.GWBApp;
import mastef_chief.gitwebbuilder.hgm.app.tasks.TaskNotificationCopiedCode;
import mastef_chief.gitwebbuilder.hgm.app.tasks.TaskNotificationCopiedLink;
import mastef_chief.gitwebbuilder.hgm.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.thegaminghuskymc.gadgetmod.api.ApplicationManager;
import net.thegaminghuskymc.gadgetmod.api.task.TaskManager;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies = Reference.DEPENDS)
public class Main {

    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event){
        TaskManager.registerTask(TaskNotificationCopiedCode.class);
        TaskManager.registerTask(TaskNotificationCopiedLink.class);
        ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "gitwebbuilder_app"), GWBApp.class);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){}

}
