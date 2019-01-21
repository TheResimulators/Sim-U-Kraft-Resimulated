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
        System.out.println("render outline " + te.isRenderoutline());
        this.bindTexture(TEXTURE_OUTLINE_BEAM);
        if (te.isRenderoutline()){
            super.render(te,x,y,z,partialTicks,destroyStage,alpha);
            drawBoundingBox(te);
        }
    }

    private void drawBoundingBox(TileMiner miner){
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.pushAttrib();
        Color c = new Color(255, 255, 255, 150);
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(0.4f);
        GlStateManager.color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.setTranslation(miner.getPos().getX(),miner.getPos().getY(),miner.getPos().getZ());
        int xposlow =  miner.getPos().getX();
        int xposhigh;
        int zposlow = miner.getPos().getZ();
        int zposhigh;
        int yposlow = miner.getPos().getY();
        int yposhigh;
        xposhigh = 1;
        zposhigh = 1;
        yposhigh = 1;
        if (miner.getMode() == 1){
            EnumFacing facing = miner.getFacing();
            yposhigh = yposlow;
            switch(facing){

                case NORTH:
                    zposhigh = -miner.getDepth();
                    xposhigh = miner.getWidth();
                    break;
                case EAST:
                    zposhigh = miner.getDepth();
                    xposhigh = miner.getWidth();
                    break;

                case SOUTH:
                    zposhigh =  miner.getDepth();
                    xposhigh = -miner.getWidth();
                    break;
                case WEST:
                    zposhigh =  miner.getDepth();
                    xposhigh = -miner.getWidth();
                    break;
            }}
            bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            //AB
            GlStateManager.pushMatrix();
            bufferBuilder.pos(xposlow,yposlow,zposlow);//A
            bufferBuilder.pos(xposhigh,yposhigh,zposlow).endVertex();//B
            //AD
            bufferBuilder.pos(xposlow,yposlow,zposlow).endVertex();//A
            bufferBuilder.pos(xposlow,yposlow,zposhigh).endVertex();//D
            //BC
            bufferBuilder.pos(xposhigh,yposhigh,zposlow).endVertex();//B
            bufferBuilder.pos(xposhigh,yposhigh,zposhigh).endVertex();//C
            //CD
            bufferBuilder.pos(xposhigh,yposhigh,zposhigh).endVertex();//C
            bufferBuilder.pos(xposlow,yposlow,zposhigh).endVertex();//D
            bufferBuilder.setTranslation(0,0,0);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
            GlStateManager.depthMask(true);
            tessellator.draw();
        }

    }
