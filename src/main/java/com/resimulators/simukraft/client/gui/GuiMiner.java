package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;

public class GuiMiner extends GuiScreen {
    String HiredSim = "";

    int guiWidth = 175;
    int guiHeight = 228;
    int buttonwidth = 200;
    int buttonheight = 20;
    int buttonoffsetheight = 40;
    String status;
    GuiButton Button1;
    GuiButton Button2;
    final int BUTTON1 = 1;
    final int BUTTON2 = 2;

    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks)
    {
        drawDefaultBackground();
        drawString(mc.fontRenderer,"Miner", (width / 2) - (buttonwidth / 3) ,height/4-10, Color.WHITE.getRGB());
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
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case BUTTON1:
                status = "hiring";
        updatebuttons();
        }
        super.actionPerformed(button);
    }

    private void updatebuttons(){
        if (status == "hiring"){
            Button1.visible = false;
            Button1.enabled = false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}





