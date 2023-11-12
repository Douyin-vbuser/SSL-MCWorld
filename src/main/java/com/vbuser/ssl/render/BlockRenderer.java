package com.vbuser.ssl.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class BlockRenderer {
    public static void onRenderWorldLast(IBlockState state, BlockPos blockPos) {
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();

        World world = mc.world;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.translate(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = blockPos.offset(side);
            IBlockState neighborState = world.getBlockState(neighbor);

            if (neighborState.getBlock() == Blocks.AIR) {
                dispatcher.renderBlock(state, blockPos, world, bufferBuilder);
            }
        }

        tessellator.draw();
        GlStateManager.disableCull();
        GlStateManager.popMatrix();
    }
}
