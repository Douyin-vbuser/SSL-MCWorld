package com.vbuser.ssl;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.security.SecureRandom;

public class CommandEncryption extends CommandBase {
    @Override
    public String getName() {
        return  "encryption";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "encryption <from x><y><z><to x><y><z>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;

            player.sendMessage(new TextComponentString("Selected Blocks from "+args[0]+" "+args[1]+" "+args[2]+" to "+args[3]+" "+args[4]+" "+args[5]));

            int i1 = Math.max(Integer.parseInt(args[0]), Integer.parseInt(args[3]));
            int j1 = Math.max(Integer.parseInt(args[1]), Integer.parseInt(args[4]));
            int k1 = Math.max(Integer.parseInt(args[2]), Integer.parseInt(args[5]));

            for(int i = Math.min(Integer.parseInt(args[0]), Integer.parseInt(args[3])); i <= i1; i++){
                for(int j = Math.min(Integer.parseInt(args[1]), Integer.parseInt(args[4])); j <= j1; j++){
                    for(int k = Math.min(Integer.parseInt(args[2]), Integer.parseInt(args[5])); k <= k1; k++){
                        encryptionBlock(i,j,k,player);
                    }
                }
            }
        }
    }

    public void encryptionBlock(int x,int y,int z,EntityPlayer player){
        World world = player.getEntityWorld();
        BlockPos pos = new BlockPos(x,y,z);
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        Block block = world.getBlockState(pos).getBlock();
        if(!Block.isEqualTo(block,Blocks.AIR)){
            SecureRandom secureRandom = new SecureRandom();
            int r = secureRandom.nextInt(129);
            int id = (Block.getIdFromBlock(block)+r)%255;
            while (id == 253 || id== 254){
                r = secureRandom.nextInt(129);
                id = (Block.getIdFromBlock(block)+r)%255;
            }
            world.setBlockState(pos, Block.getBlockById(id).getDefaultState());
            player.sendMessage(new TextComponentString("Block in ("+x+","+y+","+z+") :"+ block.getUnlocalizedName()+"->"+Block.getBlockById(id).getUnlocalizedName()));
            Main.networkWrapper.sendToServer(new PacketValue(x,y,z,id,meta));
        }
    }
}
