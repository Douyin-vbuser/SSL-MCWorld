package com.vbuser.ssl.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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

public class PacketDownload implements IMessage {

    public int x,y,z,meta;
    public String encrypted;

    public PacketDownload() {}

    public PacketDownload(int x, int y, int z, int meta, String encrypted){
        this.x = x;
        this.y = y;
        this.z = z;
        this.meta = meta;
        this.encrypted = encrypted;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.x = packetBuffer.readInt();
        this.y = packetBuffer.readInt();
        this.z = packetBuffer.readInt();
        this.meta = packetBuffer.readInt();
        this.encrypted = packetBuffer.readString(250);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeInt(x);
        packetBuffer.writeInt(y);
        packetBuffer.writeInt(z);
        packetBuffer.writeInt(meta);
        packetBuffer.writeString(encrypted);
    }

    public static class PacketDownloadHandler implements IMessageHandler<PacketDownload,IMessage>{
        @Override
        public IMessage onMessage(PacketDownload message, MessageContext ctx) {
            File saveDir = new File(Minecraft.getMinecraft().mcDataDir,"logs");
            File encryptionFile = new File(saveDir,"encryption.json");

            try{
                Map<String, Map<String,String>> existingData = new HashMap<>();
                if(encryptionFile.exists()){
                    String existingJson = FileUtils.readFileToString(encryptionFile, StandardCharsets.UTF_8);
                    existingData = new Gson().fromJson(existingJson, new TypeToken<Map<String, Map<String,String>>>(){}.getType());
                }

                String key = String.format("%d,%d,%d",message.x,message.y,message.z);
                Map<String,String> newData = new HashMap<>();
                newData.put("id",message.encrypted);
                newData.put("meta",String.valueOf(message.meta));
                existingData.put(key,newData);

                FileWriter writer = new FileWriter(encryptionFile);
                String jsonOutput = new Gson().toJson(existingData);
                writer.write(jsonOutput);
                writer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
