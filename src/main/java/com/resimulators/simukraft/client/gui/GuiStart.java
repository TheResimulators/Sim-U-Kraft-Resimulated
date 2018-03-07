package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;

public class GuiStart extends GuiScreen{
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    public int Gamemode = -1;
    private int button_width = 100;
    private int button_height = 20;
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        drawString(fontRenderer,"Welcome to Sim-u-Kraft",width/2- button_width/2,height/3 - 50, Color.WHITE.getRGB());
        drawCenteredString(fontRenderer,"Please choose a mode to play in:",width/2,height/3-30,Color.WHITE.getRGB());
        int button1x = button1.x;
        int button1y = button1.y;
        System.out.println(button1x+","+ button1y+","+(button1x+button_width)+","+(button1y-button_height)+","+mouseX+","+mouseY);
        if (button1x <= mouseX && mouseX <= (button1x + button_width)){

            if (button1y<=mouseY && mouseY <=(button1y + button_height)){
                System.out.println("its working, should be");
                drawHoveringText("Have to supply most resources."+"\n"+"Money needed to build and hire sims etc.",button1x+300,button1y-20);
            }
        }


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(button1 = new GuiButton(1,width/2 - button_width/2,height/3 - 10,button_width,button_height,"Survival"));
        buttonList.add(button2 = new GuiButton(2,width/2 - button_width/2,height/3 + 40,button_width,button_height,"Hardcore"));
        buttonList.add(button3 = new GuiButton(3,width/2 - button_width/2,height/3 + 90,button_width,button_height,"Creative"));
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
        return true;
    }
}

