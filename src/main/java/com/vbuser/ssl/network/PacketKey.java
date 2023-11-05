package com.vbuser.ssl.network;

import com.vbuser.ssl.KeyStore;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.math.BigInteger;

public class PacketKey implements IMessage {

    private String d,e,n;

    public PacketKey(){}

    public PacketKey(String d, String e, String n){
        this.d = d;
        this.e = e;
        this.n = n;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.n = packetBuffer.readString(256);
        this.e = packetBuffer.readString(256);
        this.d = packetBuffer.readString(256);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeString(n);
        packetBuffer.writeString(e);
        packetBuffer.writeString(d);
    }

    public static class PacketKeyHandler implements IMessageHandler<PacketKey,IMessage> {
        @Override
        public IMessage onMessage(PacketKey message, MessageContext ctx) {
            KeyStore.d = new BigInteger(message.d);
            KeyStore.n = new BigInteger(message.n);
            KeyStore.e = new BigInteger(message.e);
            return null;
        }
    }
}
