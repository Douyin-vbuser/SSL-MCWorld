package com.vbuser.ssl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PacketKey implements IMessage {

    public String publicKey, privateKey;

    public PacketKey(){}

    public PacketKey(String publicKey ,String privateKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.publicKey = packetBuffer.readString(200);
        this.privateKey = packetBuffer.readString(200);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeString(publicKey);
        packetBuffer.writeString(privateKey);
    }

    public static class PacketKeyHandler implements IMessageHandler<PacketKey,IMessage> {

        @Override
        public IMessage onMessage(PacketKey message, MessageContext ctx) {
            File saveDir = ctx.getServerHandler().player.getServerWorld().getSaveHandler().getWorldDirectory();
            File keyFile = new File(saveDir,"key.json");

            try{
                Map<String,String> keyData = new HashMap<>();

                if(keyFile.exists()){
                    String existingJson = FileUtils.readFileToString(keyFile, StandardCharsets.UTF_8);
                    keyData = new Gson().fromJson(existingJson, new TypeToken<Map<String, String>>() {}.getType());
                }
                keyData.put("privateKey",message.privateKey);
                keyData.put("publicKey",message.publicKey);
                FileWriter writer = new FileWriter(keyFile);
                String json = new Gson().toJson(keyData);
                writer.write(json);
                writer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
