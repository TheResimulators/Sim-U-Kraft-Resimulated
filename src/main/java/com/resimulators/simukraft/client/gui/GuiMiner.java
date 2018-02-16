package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiMiner extends GuiScreen {
    String HiredSim = "";

    int guiWidth = 175;
    int guiHeight = 228;
    int buttonwidth = 200;
    int buttonwidth2 = 100;
    int xoffset = 10;
    int yoffset = 0;
    String status;
    GuiButton Button1;
    GuiButton Button2;
    List<EntitySim> Sims = SimToHire.Sims;
    String name;
    int i;
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
        for(i = 0; i < Sims.size(); i++){
            name = Sims.get(i).getName();
            buttonList.add(i,new GuiButton(i,i*xoffset,yoffset,name));
            if (i>5){
                yoffset = 40;
            }
        }
        buttonList.add(Button1 = new GuiButton(BUTTON1,width/2-buttonwidth/2,height/4,"Hire"));

        buttonList.add(Button2 = new GuiButton(BUTTON2,width/2-buttonwidth/2,height/4+4*yoffset,"Cancel"));


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





