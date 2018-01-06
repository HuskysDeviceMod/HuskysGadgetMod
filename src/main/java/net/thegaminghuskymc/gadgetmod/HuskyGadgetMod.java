package net.thegaminghuskymc.gadgetmod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.thegaminghuskymc.gadgetmod.api.print.PrintingManager;
import net.thegaminghuskymc.gadgetmod.api.task.TaskManager;
import net.thegaminghuskymc.gadgetmod.core.io.task.TaskGetFiles;
import net.thegaminghuskymc.gadgetmod.core.io.task.TaskGetMainDrive;
import net.thegaminghuskymc.gadgetmod.core.io.task.TaskGetStructure;
import net.thegaminghuskymc.gadgetmod.core.io.task.TaskSendAction;
import net.thegaminghuskymc.gadgetmod.core.io.task.TaskSetupFileBrowser;
import net.thegaminghuskymc.gadgetmod.core.network.task.TaskConnect;
import net.thegaminghuskymc.gadgetmod.core.network.task.TaskGetDevices;
import net.thegaminghuskymc.gadgetmod.core.network.task.TaskPing;
import net.thegaminghuskymc.gadgetmod.core.print.task.TaskPrint;
import net.thegaminghuskymc.gadgetmod.entity.EntitySeat;
import net.thegaminghuskymc.gadgetmod.event.BankEvents;
import net.thegaminghuskymc.gadgetmod.event.EmailEvents;
import net.thegaminghuskymc.gadgetmod.gui.GuiHandler;
import net.thegaminghuskymc.gadgetmod.handler.PlayerEvents;
import net.thegaminghuskymc.gadgetmod.init.GadgetApps;
import net.thegaminghuskymc.gadgetmod.init.GadgetOreDictionary;
import net.thegaminghuskymc.gadgetmod.init.GadgetTileEntities;
import net.thegaminghuskymc.gadgetmod.init.RegistrationHandler;
import net.thegaminghuskymc.gadgetmod.network.PacketHandler;
import net.thegaminghuskymc.gadgetmod.programs.ApplicationPixelShop;
import net.thegaminghuskymc.gadgetmod.programs.auction.task.TaskAddAuction;
import net.thegaminghuskymc.gadgetmod.programs.auction.task.TaskBuyItem;
import net.thegaminghuskymc.gadgetmod.programs.auction.task.TaskGetAuctions;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskCheckEmailAccount;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskDeleteEmail;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskRegisterEmailAccount;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskSendEmail;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskUpdateInbox;
import net.thegaminghuskymc.gadgetmod.programs.email.task.TaskViewEmail;
import net.thegaminghuskymc.gadgetmod.programs.system.ApplicationAppStore;
import net.thegaminghuskymc.gadgetmod.programs.system.ApplicationBank;
import net.thegaminghuskymc.gadgetmod.programs.system.appStoreThings.AppStoreAppInfo;
import net.thegaminghuskymc.gadgetmod.programs.system.appStoreThings.AppStoreCategories;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskAdd;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskGetBalance;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskPay;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskRemove;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskUpdateApplicationData;
import net.thegaminghuskymc.gadgetmod.programs.system.task.TaskUpdateSystemData;
import net.thegaminghuskymc.gadgetmod.proxy.CommonProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS, acceptedMinecraftVersions = Reference.WORKING_MC_VERSION)
public class HuskyGadgetMod {

    @Instance(Reference.MOD_ID)
    public static HuskyGadgetMod instance;

    public static File modDataDir;

    public static Gson gson;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @SideOnly(Side.CLIENT)
    public static CreativeTabs deviceBlocks = new DeviceTab("gadgetBlocks");

    @SideOnly(Side.CLIENT)
    public static CreativeTabs deviceItems = new DeviceTab("gadgetItems");

    @SideOnly(Side.CLIENT)
    public static CreativeTabs deviceDecoration = new DeviceTab("gadgetDecoration");
    public static boolean HUSKY_MODE;
    private static Logger logger;

    public static final RemoteClassLoader classLoader = new RemoteClassLoader(HuskyGadgetMod.class.getClassLoader());

    public static Logger getLogger() {
        return logger;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        HUSKY_MODE = true;

        logger = event.getModLog();

        DeviceConfig.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new DeviceConfig());

        RegistrationHandler.init();

        proxy.preInit();
        
        try {
            gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .create();
            ArrayList<AppStoreAppInfo> list = HuskyGadgetMod.gson.fromJson("[\n" +
                    "  {\n" +
                    "    \"name\": \"Pixel Browser\",\n" +
                    "    \"shortDescription\": \"A web browser in mc!\",\n" +
                    "    \"description\": \"\",\n" +
                    "    \"category\": \"TOOLS\",\n" +
                    "    \"urls\": []\n" +
                    "  }\n" +
                    "]", new TypeToken<List<AppStoreAppInfo>>(){}.getType());
            System.out.println(list.get(0));

            ArrayList<AppStoreAppInfo> info = new ArrayList<>();
            info.add(new AppStoreAppInfo("Pixel Browser", "A web browser in mc!", "", AppStoreCategories.TOOLS, new ArrayList<>(), new ArrayList<>()));
            System.out.println(gson.toJson(info));
            new ApplicationAppStore().init();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

//        if (!modDataDir.exists()) modDataDir.mkdirs();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        /* Tile Entity Registering */
        GadgetTileEntities.register();

        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "seat"), EntitySeat.class, "Seat", 0, this, 80, 1, false);

        /* Packet Registering */
        PacketHandler.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        MinecraftForge.EVENT_BUS.register(new EmailEvents());
        MinecraftForge.EVENT_BUS.register(new BankEvents());

        GadgetApps.init();
        registerTasks();

        GadgetOreDictionary.init();

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    private void registerTasks() {
        // Core
        TaskManager.registerTask(TaskUpdateApplicationData.class);
        TaskManager.registerTask(TaskPrint.class);
        TaskManager.registerTask(TaskUpdateSystemData.class);
        TaskManager.registerTask(TaskConnect.class);
        TaskManager.registerTask(TaskPing.class);
        TaskManager.registerTask(TaskGetDevices.class);

        //Bank
        TaskManager.registerTask(ApplicationBank.TaskDeposit.class);
        TaskManager.registerTask(ApplicationBank.TaskWithdraw.class);
        TaskManager.registerTask(TaskGetBalance.class);
        TaskManager.registerTask(TaskPay.class);
        TaskManager.registerTask(TaskAdd.class);
        TaskManager.registerTask(TaskRemove.class);

        TaskManager.registerTask(TaskAddAuction.class);
        TaskManager.registerTask(TaskGetAuctions.class);
        TaskManager.registerTask(TaskBuyItem.class);

        //File Browser
        TaskManager.registerTask(TaskSendAction.class);
        TaskManager.registerTask(TaskSetupFileBrowser.class);
        TaskManager.registerTask(TaskGetFiles.class);
        TaskManager.registerTask(TaskGetStructure.class);
        TaskManager.registerTask(TaskGetMainDrive.class);

        //Ender Mail
        TaskManager.registerTask(TaskUpdateInbox.class);
        TaskManager.registerTask(TaskSendEmail.class);
        TaskManager.registerTask(TaskCheckEmailAccount.class);
        TaskManager.registerTask(TaskRegisterEmailAccount.class);
        TaskManager.registerTask(TaskDeleteEmail.class);
        TaskManager.registerTask(TaskViewEmail.class);

        PrintingManager.registerPrint(new ResourceLocation(Reference.MOD_ID, "picture"), ApplicationPixelShop.PicturePrint.class);
    }

}
