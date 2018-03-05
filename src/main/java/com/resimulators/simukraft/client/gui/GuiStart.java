package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class GuiStart extends GuiScreen{
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    public int Gamemode = -1;
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        drawString(fontRenderer,"Welcome to SimuKraft",width/2,height/2 - 50, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(button1 = new GuiButton(1,0,0,"Survival"));
        buttonList.add(button2 = new GuiButton(2,30,30,"Hardcore"));
        buttonList.add(button3 = new GuiButton(3,60,60,"Creative"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:{
                Gamemode = 1;
                updatebuttons(Gamemode);
            }
            case 2:{
                Gamemode = 2;
                updatebuttons(Gamemode);
            }
            case 3: {
                Gamemode = 3;
                updatebuttons(Gamemode);
            }
        }

        super.actionPerformed(button);
    }

    private void updatebuttons(int gamemode){
        if (gamemode != -1) {
            this.mc.displayGuiScreen(null);
        }

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

