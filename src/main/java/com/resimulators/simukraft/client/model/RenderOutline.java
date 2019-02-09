package com.resimulators.simukraft.client.model;

import com.resimulators.simukraft.common.tileentity.TileMiner;
import com.resimulators.simukraft.network.MinerUpdateDataPacket;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLSync;

import java.awt.*;

public class RenderOutline extends TileEntitySpecialRenderer<TileMiner>{
    private static final ResourceLocation TEXTURE_OUTLINE_BEAM= new ResourceLocation("textures/entity/beacon_beam.png");
    public RenderOutline(){}

    @SideOnly(Side.CLIENT)
    @Override
    public void render(TileMiner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
        if (te.isRenderoutline() && (te.getMode() == 1 || te.getMode() == 0)){
            super.render(te,x,y,z,partialTicks,destroyStage,alpha);
            drawBoundingBox(te);
        }
    }

    private void drawBoundingBox(TileMiner miner){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        this.bindTexture(TEXTURE_OUTLINE_BEAM);
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        Color c = new Color(255, 255, 255, 150);
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(2f);
        int xposlow =  miner.getPos().getX();
        int xposhigh;
        int zposlow = miner.getPos().getZ();
        int zposhigh;
        int yposlow = miner.getPos().getY();
        int yposhigh;
        xposhigh = 1;
        zposhigh = 1;
        yposhigh = 1;
        if (true){
            EnumFacing facing = miner.getFacing();
            switch(facing){

                case NORTH:
                    xposhigh = miner.getPos().getX()+miner.getWidth();
                    zposhigh = miner.getPos().getZ()-miner.getDepth();
                    break;
                case EAST:
                    zposhigh = miner.getPos().getX() + miner.getDepth();
                    xposhigh = miner.getPos().getZ() + miner.getWidth();
                    break;

                case SOUTH:
                    zposhigh =miner.getPos().getX() + miner.getDepth();
                    xposhigh =miner.getPos().getZ() -miner.getWidth();
                    break;
                case WEST:
                    zposhigh =miner.getPos().getX() + miner.getDepth();
                    xposhigh =miner.getPos().getZ() -miner.getWidth();
                    break;
                default:
                    System.out.println("this should not happen");
            }}
            bufferBuilder.setTranslation(miner.getPos().getX(),miner.getPos().getY(),miner.getPos().getZ());
            bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            bufferBuilder.pos(1f,0.75f,1f).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(1f,0.25f,1f).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(1f,0.25f,0).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(1f,0.75f,0).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(4f,0.75f,0).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(4f,0.25f,0).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(4f,0.25f,1f).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            bufferBuilder.pos(4f,0.75f,1f).color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha()).endVertex();
            tessellator.draw();
            bufferBuilder.setTranslation(0,0,0);
            GlStateManager.depthMask(true);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();



        }



        @Override
        public boolean isGlobalRenderer(TileMiner miner){
        return true;
        }

    }
