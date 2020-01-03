package com.resimulators.simukraft.client.model;

import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderBuildingPlacement extends TileEntitySpecialRenderer<TileConstructor> {

    private boolean render = false;


    @Override
    public void render(TileConstructor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha){
      super.render(te,x,y,z,partialTicks,destroyStage,alpha);
        //System.out.println(te.isRender());
        if (te.isRender() && te.getPosA() != null){
            Vec3d posA = new Vec3d(te.getPosA().getX(),te.getPosA().getY(),te.getPosA().getZ());
           Vec3d posB = new Vec3d(te.getPosB().getX(),te.getPosB().getY(),te.getPosB().getZ());
           drawBoundingBox( posA,posB,2,new Color(255, 255, 255, 150));

        }


    }

    private void drawBoundingBox(Vec3d posA, Vec3d posB,float width, Color c) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glLineWidth(width);
        GL11.glDepthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        double lowX;
        double lowY;
        double lowZ;
        double highX;
        double highY;
        double highZ;

        if (posA.x > posB.x) {
            lowX = posB.x;
            highX = posA.x;
        } else {
            lowX = posA.x;
            highX = posB.x;
        }
        if (posA.y > posB.y) {
            lowY = posB.y;
            highY = posA.y;
        } else {
            lowY = posA.y;
            highY = posB.y;
        }
        if (posA.z > posB.z) {
            lowZ = posB.z;
            highZ = posA.z;
        } else {
            lowZ = posA.z;
            highZ = posB.z;
        }


        Vec3d low = new Vec3d(lowX, lowY, lowZ);
        Vec3d high = new Vec3d(highX, highY, highZ);

        low = low.add(-0.01, -0.01, -0.01);
        double dx = Math.abs(low.x - high.x) + 1.01;
        double dy = Math.abs(low.y - high.y) + 1.01;
        double dz = Math.abs(low.z - high.z) + 1.01;

        //AB
        bufferBuilder.pos(low.x, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        bufferBuilder.pos(low.x, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        //BC
        bufferBuilder.pos(low.x, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        bufferBuilder.pos(low.x+dx, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        //CD
        bufferBuilder.pos(low.x+dx, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        bufferBuilder.pos(low.x+dx, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        //DA
        bufferBuilder.pos(low.x+dx, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        bufferBuilder.pos(low.x, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        //EF
        bufferBuilder.pos(low.x, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        bufferBuilder.pos(low.x, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        //FG
        bufferBuilder.pos(low.x, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        //GH
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H
        //HE
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H
        bufferBuilder.pos(low.x, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        //AE
        bufferBuilder.pos(low.x, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();          //A
        bufferBuilder.pos(low.x, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //E
        //BF
        bufferBuilder.pos(low.x, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //B
        bufferBuilder.pos(low.x, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //F
        //CG
        bufferBuilder.pos(low.x+dx, low.y, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //C
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z+dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex(); //G
        //DH
        bufferBuilder.pos(low.x+dx, low.y, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();       //D
        bufferBuilder.pos(low.x+dx, low.y+dy, low.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();    //H

        tessellator.draw();

        tessellator.getBuffer().setTranslation(0, 0, 0);

        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }





}