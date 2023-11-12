package com.vbuser.ssl.command;

import com.vbuser.ssl.KeyStore;
import com.vbuser.ssl.Main;
import com.vbuser.ssl.network.PacketRequest;
import com.vbuser.ssl.render.TestRender;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandDownLoad extends CommandBase {
    @Override
    public String getName() {
        return "download";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "download";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)  {
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            KeyStore.client_key_count = 0;
            Main.networkWrapper.sendToServer(new PacketRequest(player.getUniqueID().toString(), KeyStore.e.toString(),KeyStore.n.toString()));
            TestRender.loaded = true;
        }
    }

    public int getRequiredPermissionLevel(){return 2;}
}
