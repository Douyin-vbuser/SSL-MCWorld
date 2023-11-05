package com.vbuser.ssl.command;

import com.vbuser.ssl.KeyStore;
import com.vbuser.ssl.Main;
import com.vbuser.ssl.network.PacketRequest;
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
            Main.networkWrapper.sendToServer(new PacketRequest(player.getUniqueID().toString(), KeyStore.e.toString(),KeyStore.n.toString()));
        }
    }
}
