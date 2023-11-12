package com.vbuser.ssl;

import com.vbuser.ssl.command.CommandDownLoad;
import com.vbuser.ssl.command.CommandEncryption;
import com.vbuser.ssl.command.CommandKeyGen;
import com.vbuser.ssl.command.CommandRender;
import com.vbuser.ssl.network.PacketDownload;
import com.vbuser.ssl.network.PacketKey;
import com.vbuser.ssl.network.PacketRequest;
import com.vbuser.ssl.network.PacketValue;
import com.vbuser.ssl.render.TestRender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "ssl",name = "ssl",version = "alpha 1.0.0")
public class Main {

    public static SimpleNetworkWrapper networkWrapper;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("SSLChannel");
        networkWrapper.registerMessage(PacketValue.PacketValueHandler.class,PacketValue.class,0, Side.SERVER);
        networkWrapper.registerMessage(PacketKey.PacketKeyHandler.class,PacketKey.class,1, Side.CLIENT);
        networkWrapper.registerMessage(PacketRequest.PacketRequestHandler.class,PacketRequest.class,2,Side.SERVER);
        networkWrapper.registerMessage(PacketDownload.PacketDownloadHandler.class,PacketDownload.class,3,Side.CLIENT);
    }

    @Mod.EventHandler
    public static void serverInit(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandEncryption());
        event.registerServerCommand(new CommandKeyGen());
        event.registerServerCommand(new CommandDownLoad());
        event.registerServerCommand(new CommandRender());
    }

    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new TestRender());
    }
}
