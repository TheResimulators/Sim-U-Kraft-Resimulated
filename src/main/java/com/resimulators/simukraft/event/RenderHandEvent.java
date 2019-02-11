package com.resimulators.simukraft.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.item.ItemBlueprint;
import com.resimulators.simukraft.common.item.ItemPlanningSheet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by fabbe on 23/10/2018 - 7:12 PM.
 */
public class RenderHandEvent {

    private int offsetX = 1;
    private int offsetY = 1;

    private EntityPlayer player;
    private Vec3d playerPos;

    //Planning Sheet
    private boolean holdingPlanningSheet = false;
    private BlockPos pos1;
    private BlockPos pos2;

    //Blueprint
    private boolean holdingBlueprint = false;
    private String name;
    private String author;
    private BlockPos startPos;
    private BlockPos size;
    private BlockPos chestPos;
    private Template template;
    private List<Template.BlockInfo> blocks;

    @SubscribeEvent
    public void playerEvent(RenderSpecificHandEvent event) {
        ItemStack stack = event.getItemStack();
        player = Minecraft.getMinecraft().player;
        if (stack.getItem() instanceof ItemPlanningSheet && event.getHand().equals(EnumHand.MAIN_HAND)) {
            ItemPlanningSheet sheet = (ItemPlanningSheet) stack.getItem();
            pos1 = sheet.getBlockPos1(stack);
            pos2 = sheet.getBlockPos2(stack);
            holdingPlanningSheet = true;
        } else if (!(stack.getItem() instanceof ItemPlanningSheet) && event.getHand().equals(EnumHand.MAIN_HAND)) {
            holdingPlanningSheet = false;
        }
        if (stack.getItem() instanceof ItemBlueprint && event.getHand().equals(EnumHand.MAIN_HAND)) {
            ItemBlueprint blueprint = (ItemBlueprint) stack.getItem();
            name = blueprint.getStructure(stack);
            author = blueprint.getAuthor(stack);
            startPos = blueprint.getStartPos(stack);
            chestPos = blueprint.getChestPos(stack);
            template = blueprint.getTemplate();
            if (template != null)
                size = template.getSize();
            try {
                Field field = template.getClass().getDeclaredField("blocks");
                field.setAccessible(true);
                blocks = (List<Template.BlockInfo>) field.get(template);
            } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {}
            holdingBlueprint = true;
        } else if (!(stack.getItem() instanceof ItemBlueprint) && event.getHand().equals(EnumHand.MAIN_HAND)) {
            holdingBlueprint = false;
            blocks = null;
            template = null;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if ((pos1 != null && pos2 != null) && holdingPlanningSheet) {
            double xPos = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
            double yPos = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
            double zPos = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();
            playerPos = new Vec3d(xPos, yPos, zPos);
            if (player != null) {
                if (player.getPosition().getDistance(pos1.getX(), pos1.getY(), pos1.getZ()) < 512.0D)
                    this.drawBoundingBox(playerPos, new Vec3d(pos1), new Vec3d(pos2), 2f, new Color(255, 255, 255, 150));
            }
        }
        if ((startPos != null && chestPos != null && name != null && author != null) && holdingBlueprint) {
            double xPos = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
            double yPos = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
            double zPos = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();
            playerPos = new Vec3d(xPos, yPos, zPos);
            if (player != null) {
                if (player.getPosition().getDistance(startPos.getX(), startPos.getY(), startPos.getZ()) < 512.0D)
                    this.drawBoundingBox(playerPos, new Vec3d(startPos), new Vec3d(startPos.add(size.getX() - 1, size.getY() - 1, size.getZ() - 1)), 2f, new Color(255, 255, 255, 150));
                if (player.getPosition().getDistance(chestPos.getX(), chestPos.getY(), chestPos.getZ()) < 512.0D)
                    this.drawBoundingBox(playerPos, new Vec3d(chestPos), new Vec3d(chestPos), 2f, new Color(100, 255, 100, 150));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if ((pos1 != null && pos2 != null) && holdingPlanningSheet) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            String pos1Line = "Pos 1: " + Utilities.formatBlockPos(pos1);
            String pos2Line = "Pos 2: " + Utilities.formatBlockPos(pos2);

            int width = renderer.getStringWidth(pos1Line);
            if (width < renderer.getStringWidth(pos2Line))
                width = renderer.getStringWidth(pos2Line);

            renderer.drawString(pos1Line, event.getResolution().getScaledWidth() - width - offsetX, offsetY, 0xFFFFFF, true);
            renderer.drawString(pos2Line, event.getResolution().getScaledWidth() - width - offsetX, 10 + offsetY, 0xFFFFFF, true);
        }
        if ((startPos != null && size != null && chestPos != null && name != null && author != null) && holdingBlueprint) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            if (name.equals(""))
                name = ChatFormatting.DARK_RED + "Structure not set.";
            if (author.equals(""))
                author = ChatFormatting.DARK_RED + "Author not set.";

            String nameLine = "Name: " + ChatFormatting.DARK_PURPLE + name;
            String authorLine = "Author: " + ChatFormatting.DARK_PURPLE + author;
            String startPosLine = "Start Position: " + Utilities.formatBlockPos(startPos);
            String sizePosLine = "Size: " + Utilities.formatBlockPos(size);
            String chestPosLine = "Chest Position: " + Utilities.formatBlockPos(chestPos);

            int width = renderer.getStringWidth(nameLine);
            if (width < renderer.getStringWidth(authorLine))
                width = renderer.getStringWidth(authorLine);
            if (width < renderer.getStringWidth(startPosLine))
                width = renderer.getStringWidth(startPosLine);
            if (width < renderer.getStringWidth(sizePosLine))
                width = renderer.getStringWidth(sizePosLine);
            if (width < renderer.getStringWidth(chestPosLine))
                width = renderer.getStringWidth(chestPosLine);


            renderer.drawString(nameLine, event.getResolution().getScaledWidth() - width - offsetX, offsetY, 0xFFFFFF, true);
            renderer.drawString(authorLine, event.getResolution().getScaledWidth() - width - offsetX, 10 + offsetY, 0xFFFFFF, true);
            renderer.drawString(startPosLine, event.getResolution().getScaledWidth() - width - offsetX, 20 + offsetY, 0xFFFFFF, true);
            renderer.drawString(chestPosLine, event.getResolution().getScaledWidth() - width - offsetX, 30 + offsetY, 0xFFFFFF, true);
        }
    }

    private void drawBoundingBox(Vec3d player_pos, Vec3d posA, Vec3d posB, float width, Color c) {
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

        bufferBuilder.setTranslation(-player_pos.x, -player_pos.y, -player_pos.z);

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
