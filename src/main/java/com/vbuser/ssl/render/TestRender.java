package com.vbuser.ssl.render;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vbuser.ssl.KeyGenerate;
import com.vbuser.ssl.KeyStore;
import com.vbuser.ssl.Main;
import com.vbuser.ssl.network.PacketDownload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

public class TestRender {
    @SubscribeEvent
    public void onRenderWorld(RenderBlockOverlayEvent event) {
        File saveDir = new File(Minecraft.getMinecraft().mcDataDir,"logs");
        File encryptionFile = new File(saveDir,"encryption.json");

        if(encryptionFile.exists()){
            try {
                String filePath = encryptionFile.getAbsolutePath();
                FileReader reader = new FileReader(filePath);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
                Gson gson = new Gson();
                Map<String, Map<String, String>> data = gson.fromJson(jsonElement, new TypeToken<Map<String, Map<String, Integer>>>(){}.getType());

                for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
                    String key = entry.getKey();
                    BlockPos blockPos = event.getBlockPos();
                    String pos = blockPos.getX() +","+ blockPos.getY() +","+ blockPos.getZ();
                    if(entry.getValue().containsKey(pos)){
                        String id = entry.getValue().get("id");
                        int meta = Integer.parseInt(entry.getValue().get("meta"));
                        int a = Integer.parseInt(KeyGenerate.decrypt( new BigInteger(id), KeyStore.d,KeyStore.n).toString());
                    }
                }
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
