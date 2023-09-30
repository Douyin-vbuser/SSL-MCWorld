package com.vbuser.ssl;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
        networkWrapper.registerMessage(PacketValue.PacketValueHandler.class, PacketValue.class, 0, Side.SERVER);
        networkWrapper.registerMessage(PacketKey.PacketKeyHandler.class, PacketKey.class , 1 ,Side.SERVER);
    }

    @Mod.EventHandler
    public static void serverInit(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandEncryption());
        event.registerServerCommand(new CommandKeyGen());
    }
}
