package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GuiBuilding extends GuiScreen {


    private categoryButton commercialButton;
    private categoryButton industrialButton;
    private categoryButton residentialButton;
    private categoryButton customButton;
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX,mouseY,partialTicks);
    }


    @Override
    public void initGui()
    {
        buttonList.add(residentialButton = new categoryButton(0,width/2-105,height/2-10,"Residential"));
        buttonList.add(industrialButton = new categoryButton(1,width/2+5,height/2-10,"Industrial"));
        buttonList.add(commercialButton = new categoryButton(2,width/2-105,height/2+10,"Commercial"));
        buttonList.add(customButton = new categoryButton(3,width/2+5,height/2+10,"Custom"));

        super.initGui();
    }


    @Override
    protected void actionPerformed(GuiButton button)
    {

    }





    private class categoryButton extends GuiButton
    {
     private categoryButton(int id, int x, int y, String string)
        {
            super(id,x,y,120,20,string);
        }
    }
    {

    }
}
