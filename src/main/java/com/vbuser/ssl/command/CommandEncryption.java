package com.vbuser.ssl.command;

import com.vbuser.ssl.Main;
import com.vbuser.ssl.network.PacketValue;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.security.SecureRandom;

public class CommandEncryption extends CommandBase {
    @Override
    public String getName() {
        return "encryption";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "encryption <from x><y><z><to x><y><z>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;

            int i1 = Math.max(Integer.parseInt(args[0]),Integer.parseInt(args[3]));
            int j1 = Math.max(Integer.parseInt(args[1]),Integer.parseInt(args[4]));
            int k1 = Math.max(Integer.parseInt(args[2]),Integer.parseInt(args[5]));

            for(int i = Math.min(Integer.parseInt(args[0]),Integer.parseInt(args[3])); i <= i1; i++){
                for(int j = Math.min(Integer.parseInt(args[1]),Integer.parseInt(args[4])); j <= j1; j++){
                    for(int k = Math.min(Integer.parseInt(args[2]),Integer.parseInt(args[5])); k <= k1; k++){
                        encryption(i,j,k,player);
                    }
                }
            }
        }
    }

    public static void encryption(int x,int y,int z,EntityPlayer player){
        World world = player.getEntityWorld();
        BlockPos pos = new BlockPos(x,y,z);
        Block block = world.getBlockState(pos).getBlock();
        int meta = block.getMetaFromState(world.getBlockState(pos));
        if(!Block.isEqualTo(block, Blocks.AIR)){
            SecureRandom random = new SecureRandom();
            int r = random.nextInt(129);
            int id = (Block.getIdFromBlock(block)+r)%255;
            world.setBlockState(pos,Block.getBlockById(id).getDefaultState());
            Main.networkWrapper.sendToServer(new PacketValue(x,y,z,id,meta));
        }
    }
}
