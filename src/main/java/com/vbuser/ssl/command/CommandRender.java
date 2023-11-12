package com.vbuser.ssl.command;

import com.vbuser.ssl.render.TestRender;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandRender extends CommandBase {
    @Override
    public String getName() {
        return "render";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "render";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if(sender instanceof EntityPlayer){
            TestRender.loaded = false;
        }
    }

    public int getRequiredPermissionLevel(){return 2;}
}
