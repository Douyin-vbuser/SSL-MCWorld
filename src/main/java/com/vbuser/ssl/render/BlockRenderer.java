package com.vbuser.ssl.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BlockRenderer {

    @SubscribeEvent
    public void onRenderBlock(TickEvent.RenderTickEvent event) {
        if(Minecraft.getMinecraft().player!=null) {
            Minecraft mc = Minecraft.getMinecraft();
            World world = mc.world;

            BlockPos blockPos = new BlockPos(6,6,6);

            IBlockState state = Blocks.GRASS.getDefaultState();

            GlStateManager.pushMatrix();

            GlStateManager.translate(
                    -mc.getRenderManager().viewerPosX,
                    -mc.getRenderManager().viewerPosY,
                    -mc.getRenderManager().viewerPosZ
            );

            RenderGlobal.drawSelectionBoundingBox(
                    state.getSelectedBoundingBox(world, blockPos)
                            .grow(0.002D)
                            .offset(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ()),
                    1.0F, 1.0F, 1.0F, 1.0F
            );

            GlStateManager.popMatrix();
        }
    }
}
