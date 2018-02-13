package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;


import java.awt.*;
import java.io.IOException;

public class GuiFarm extends GuiScreen {
    String HiredSim = "";

    int guiWidth = 175;
    int guiHeight = 228;
    int buttonwidth = 200;
    int buttonheight = 20;
    int buttonoffsetheight = 40;
    GuiButton Button1;
    GuiButton Button2;
    int BUTTON1 = 1;
    int BUTTON2 = 2;

    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks)
    {
        drawDefaultBackground();
        drawString(mc.fontRenderer,"Farmer", (width / 2) - (buttonwidth / 3) ,height/4-10, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(Button1 = new GuiButton(BUTTON1,width/2-buttonwidth/2,height/4+0,"Hire"));
        buttonList.add(Button2 = new GuiButton(BUTTON2,width/2-buttonwidth/2,height/4+4*buttonoffsetheight,"Cancel"));


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
