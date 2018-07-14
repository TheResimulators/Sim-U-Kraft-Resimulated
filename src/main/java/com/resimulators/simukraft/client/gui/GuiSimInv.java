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

    public GuiSimInv(IInventory playerInv, EntitySim sim) {
        super(new SimContainer(playerInv, sim));
        this.xSize = 175;
        this.ySize = 165;
        this.playerInv = playerInv;
        this.sim = sim;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/guis/simgui.png"));
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
        String s = I18n.format("container.SimContainer");
        this.mc.fontRenderer.drawString(s, this.xSize - this.mc.fontRenderer.getStringWidth(s) / 2 - 10, 43, 4210752);
        this.mc.fontRenderer.drawString(this.playerInv.getDisplayName().getFormattedText(), 130, 110, 4210752);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
