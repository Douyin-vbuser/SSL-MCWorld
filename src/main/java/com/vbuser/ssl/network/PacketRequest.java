package com.vbuser.ssl.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vbuser.ssl.KeyGenerate;
import com.vbuser.ssl.KeyStore;
import com.vbuser.ssl.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

public class PacketRequest implements IMessage {

    public String UUID;
    public String e,n;

    public PacketRequest() {
    }

    public PacketRequest(String uuid,String e,String n) {
        this.UUID = uuid;
        this.e = e;
        this.n = n;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.UUID = packetBuffer.readString(250);
        this.n = packetBuffer.readString(250);
        this.e = packetBuffer.readString(250);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeString(this.UUID);
        packetBuffer.writeString(this.n);
        packetBuffer.writeString(this.e);
    }

    public static class PacketRequestHandler implements IMessageHandler<PacketRequest, IMessage> {
        @Override
        public IMessage onMessage(PacketRequest message, MessageContext ctx) {
            KeyStore.s_e = new BigInteger(message.e);
            KeyStore.s_n = new BigInteger(message.n);

            File saveDir = ctx.getServerHandler().player.getServerWorld().getSaveHandler().getWorldDirectory();
            File encryptionFile = new File(saveDir,"encryption.json");
            if(encryptionFile.exists()) {
                try {
                    String filePath = encryptionFile.getAbsolutePath();
                    FileReader reader = new FileReader(filePath);
                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(reader);
                    Gson gson = new Gson();
                    Map<String, Map<String, Integer>> data = gson.fromJson(jsonElement, new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());

                    for (Map.Entry<String, Map<String, Integer>> entry : data.entrySet()) {
                        String key = entry.getKey();
                        int id = entry.getValue().get("id");
                        int meta = entry.getValue().get("meta");
                        String[] parts = key.split(",");
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        int z = Integer.parseInt(parts[2]);

                        String encrypted = KeyGenerate.encrypt(id,new BigInteger(message.e),new BigInteger(message.n)).toString();
                        Main.networkWrapper.sendTo(new PacketDownload(x,y,z,meta,encrypted), (EntityPlayerMP) ctx.getServerHandler().player.getServerWorld().getPlayerEntityByUUID(java.util.UUID.fromString(message.UUID)));
                    }
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return null;
        }
    }
}