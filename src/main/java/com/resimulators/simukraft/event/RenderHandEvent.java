package com.resimulators.simukraft.event;

import com.resimulators.simukraft.common.item.ItemPlanningSheet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by fabbe on 23/10/2018 - 7:12 PM.
 */
public class RenderHandEvent {
    private EntityPlayer player;
    private Vec3d playerPos;
    private boolean holdingItem = false;
    private BlockPos pos1;
    private BlockPos pos2;

    @SubscribeEvent
    public void playerEvent(RenderSpecificHandEvent event) {
        ItemStack stack = event.getItemStack();
        player = Minecraft.getMinecraft().player;
        if (stack.getItem() instanceof ItemPlanningSheet && event.getHand().equals(EnumHand.MAIN_HAND)) {
            ItemPlanningSheet sheet = (ItemPlanningSheet) stack.getItem();
            pos1 = sheet.getBlockPos1(stack);
            pos2 = sheet.getBlockPos2(stack);
            if (pos1 != null && pos2 != null) {
                holdingItem = true;
            }
        } else if (!(stack.getItem() instanceof ItemPlanningSheet) && event.getHand().equals(EnumHand.MAIN_HAND)) {
            holdingItem = false;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if ((pos1 != null && pos2 != null) && holdingItem) {
            double xPos = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
            double yPos = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
            double zPos = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();
            playerPos = new Vec3d(xPos, yPos, zPos);
            if (player.getPosition().getDistance(pos1.getX(), pos1.getY(), pos1.getZ()) < 512.0D)
                drawBoundingBox(playerPos, new Vec3d(pos1), new Vec3d(pos2), 2f);
        }
    }

    private static void drawBoundingBox(Vec3d player_pos, Vec3d posA, Vec3d posB, float width) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(-player_pos.x, -player_pos.y, -player_pos.z);


        Color c = new Color(255, 255, 255, 150);
        GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
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

        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }
}
