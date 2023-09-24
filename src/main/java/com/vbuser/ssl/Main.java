package com.vbuser.ssl;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "ssl",name = "ssl",version = "basic 1.0.0")
public class Main {

    @Mod.EventHandler
    public static void serverInit(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandEncryption());
    }
}
