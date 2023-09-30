package com.vbuser.ssl;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.math.BigInteger;

public class CommandKeyGen extends CommandBase {
    @Override
    public String getName() {
        return "key_gen";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "key_gen";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if(sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            KeyGenerate.time = System.currentTimeMillis();
            BigInteger p = KeyGenerate.generatePrimeFromIPAddress(KeyGenerate.getServerIPAddress("127.0.0.1"));
            BigInteger q = KeyGenerate.generatePrimeFromTime();
            BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
            BigInteger e = KeyGenerate.findValidE(phi_n);
            BigInteger d = KeyGenerate.computeModularInverse(e, phi_n);
            player.sendMessage(new TextComponentString("Generated Public Key for SSL:" + e));
            player.sendMessage(new TextComponentString("Generated Private Key for SSL:" + d));
            String publicKey = e.toString();
            String privateKey = d.toString();
            Main.networkWrapper.sendToServer(new PacketKey(publicKey, privateKey));
        }
    }
}
