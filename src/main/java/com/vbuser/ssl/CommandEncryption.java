package com.vbuser.ssl;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.math.BigInteger;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            KeyGenerate.time = server.getCurrentTime();
            BigInteger p = KeyGenerate.generatePrimeFromIPAddress(KeyGenerate.getServerIPAddress(server.getServerHostname()));
            player.sendMessage(new TextComponentString("Generating Public Key for SSL"));

            player.sendMessage(new TextComponentString("Selected Blocks from "+args[0]+" "+args[1]+" "+args[2]+"to"+args[3]+" "+args[4]+" "+args[5]));
            for(int i= Integer.parseInt(args[0]) ; i <= Integer.parseInt(args[3]) ; i++){
                for(int j= Integer.parseInt(args[1]) ; j <= Integer.parseInt(args[4]) ; j++){
                    for(int k= Integer.parseInt(args[2]) ; k <= Integer.parseInt(args[5]) ; k++){
                        BigInteger q = KeyGenerate.generatePrimeFromBlockPos(new BlockPos(i,j,k));
                        BigInteger n = p.multiply(q);
                        BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
                        BigInteger e = KeyGenerate.findValidE(phi_n);
                        BigInteger d = KeyGenerate.computeModularInverse(e,phi_n);
                        player.sendMessage(new TextComponentString("Key Generated for block"+i+" "+j+" "+k));
                        player.sendMessage(new TextComponentString("Public Key:("+n+","+e+")"));
                        player.sendMessage(new TextComponentString("Private Key:("+n+","+d+")"));
                        //encryption(Caesar cipher)
                    }
                }
            }
        }
    }
}
