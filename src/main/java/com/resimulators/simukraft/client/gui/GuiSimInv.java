


package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.containers.SimContainer;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiSimInv extends GuiContainer {
    private IInventory playerInv;
    private EntitySim sim;
    private String s = I18n.format("container.SimContainer");
    private String ss = I18n.format("container.SimContainer.drops");
    public GuiSimInv(IInventory playerInv, EntitySim sim) {
        super(new SimContainer(playerInv, sim));
        this.xSize = 175;
        this.ySize = 173;
        this.playerInv = playerInv;
        this.sim = sim;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/guis/simgui.png"));
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);

        this.mc.fontRenderer.drawString(s, width/2 - this.mc.fontRenderer.getStringWidth(s)/2- 20, height/2-ySize/2+5, 4210752);
        this.mc.fontRenderer.drawString(this.playerInv.getDisplayName().getFormattedText(), width/2-this.mc.fontRenderer.getStringWidth(this.playerInv.getDisplayName().getFormattedText())-30, height/2-6, 4210752);
        this.mc.fontRenderer.drawString(ss,width/2 - this.mc.fontRenderer.getStringWidth(ss)/2- 30,(height/3)-12,4210752);
    }




    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}

