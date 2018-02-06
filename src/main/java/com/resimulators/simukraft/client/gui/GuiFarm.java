package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;


import java.awt.*;
import java.io.IOException;

public class GuiFarm extends GuiScreen {
    String HiredSim = "";

    int guiWidth = 175;
    int guiHeight = 228;
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX,mouseY, partialTicks);
        drawString(mc.fontRenderer, "Gender:",guiWidth,guiHeight, Color.WHITE.getRGB() );
    }
    @Override
    public void initGui() {
        super.initGui();
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        super.actionPerformed(button);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
