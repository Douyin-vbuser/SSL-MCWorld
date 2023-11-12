package com.vbuser.ssl.render;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.vbuser.ssl.KeyGenerate;
import com.vbuser.ssl.KeyStore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

@SuppressWarnings("all")
public class TestRender {

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event){
        if(Minecraft.getMinecraft().player!=null) {

            BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

            File saveDir = new File(Minecraft.getMinecraft().mcDataDir, "logs");
            File encryptionFile = new File(saveDir, "encryption.json");

            if(KeyStore.e==null){
                encryptionFile.delete();
            }

            if (encryptionFile.exists() && KeyStore.e!=null && KeyStore.client_key_count==KeyStore.server_key_count) {
                try {
                    String filePath = encryptionFile.getAbsolutePath();
                    FileReader reader = new FileReader(filePath);
                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(reader);
                    Gson gson = new Gson();
                    Map<String, Map<String, String>> data = gson.fromJson(jsonElement, new TypeToken<Map<String, Map<String, String>>>() {
                    }.getType());

                    for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

                        String key = entry.getKey();
                        String[] parts = key.split(",");
                        if (parts.length >= 3) {
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);
                            int z = Integer.parseInt(parts[2]);
                            String id = entry.getValue().get("id");
                            int meta = Integer.parseInt(entry.getValue().get("meta"));
                            int result = Integer.parseInt(KeyGenerate.decrypt(new BigInteger(id), KeyStore.d, KeyStore.n).toString());
                            IBlockState state = Block.getBlockById(result).getStateFromMeta(meta);
                            BlockPos pos = new BlockPos(x, y, z);

                            BlockRenderer.onRenderWorldLast(state,pos);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
