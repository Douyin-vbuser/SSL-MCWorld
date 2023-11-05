package com.vbuser.ssl.command;

import com.vbuser.ssl.KeyGenerate;
import com.vbuser.ssl.KeyStore;
import com.vbuser.ssl.Main;
import com.vbuser.ssl.network.PacketKey;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandKeyGen extends CommandBase {
    @Override
    public String getName() {
        return "gen_key";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "gen_key";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if(sender instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) sender;
            KeyGenerate.time = System.currentTimeMillis();
            try {
                InetAddress address = InetAddress.getLocalHost();
                String ip = address.toString();
                BigInteger p = KeyGenerate.generatePrimeFromTime();
                BigInteger q = KeyGenerate.generatePrimeFromIPAddress(ip);
                BigInteger n = p.multiply(q);
                BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
                BigInteger e = KeyGenerate.findValidE(phi_n);
                BigInteger d = KeyGenerate.computeModularInverse(e,phi_n);
                Main.networkWrapper.sendToServer(new PacketKey(d.toString(),e.toString(),n.toString()));

                KeyStore.e = e;
                KeyStore.n = n;
                KeyStore.d = d;

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
