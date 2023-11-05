package com.vbuser.ssl.network;

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

public class PacketValue implements IMessage {

    private int x,y,z,id,meta;

    public PacketValue(){
    }

    public PacketValue(int x,int y,int z,int id,int meta){
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.meta = meta;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        x = packetBuffer.readInt();
        y = packetBuffer.readInt();
        z = packetBuffer.readInt();
        id = packetBuffer.readInt();
        meta = packetBuffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffet = new PacketBuffer(buf);
        buffet.writeInt(x);
        buffet.writeInt(y);
        buffet.writeInt(z);
        buffet.writeInt(id);
        buffet.writeInt(meta);
    }

    public static class PacketValueHandler implements IMessageHandler<PacketValue,IMessage>{

        @Override
        public IMessage onMessage(PacketValue message, MessageContext ctx) {
            File saveDir = ctx.getServerHandler().player.getServerWorld().getSaveHandler().getWorldDirectory();
            File encryptionFile = new File(saveDir,"encryption.json");

            try{
                Map<String, Map<String,Integer>> existingData = new HashMap<>();
                if(encryptionFile.exists()){
                    String existingJson = FileUtils.readFileToString(encryptionFile, StandardCharsets.UTF_8);
                    existingData = new Gson().fromJson(existingJson, new TypeToken<Map<String, Map<String,Integer>>>(){}.getType());
                }

                String key = String.format("%d,%d,%d",message.x,message.y,message.z);
                Map<String,Integer> newData = new HashMap<>();
                newData.put("id",message.id);
                newData.put("meta",message.meta);
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
