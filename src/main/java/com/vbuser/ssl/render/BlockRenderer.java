package com.vbuser.ssl.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class BlockRenderer {
    public static void onRenderWorldLast(IBlockState state,BlockPos blockPos) {
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        dispatcher.renderBlock(state, blockPos, mc.world, tessellator.getBuffer());

        tessellator.draw();
        GlStateManager.popMatrix();
    }
}
