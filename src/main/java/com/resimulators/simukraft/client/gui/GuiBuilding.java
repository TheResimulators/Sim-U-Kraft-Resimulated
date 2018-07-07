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
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
    }


    @Override
    public void initGui()
    {
        buttonList.add(residentialButton = new categoryButton(0,100,height/2,"Residential"));
        buttonList.add(industrialButton = new categoryButton(1,300,height/2,"Industrial"));
        buttonList.add(commercialButton = new categoryButton(2,500,height/2,"Commercial"));
    }


    @Override
    protected void actionPerformed(GuiButton button)
    {

    }





    private class categoryButton extends GuiButton
    {
     private categoryButton(int id, int x, int y, String string)
        {
            super(id,x,y,string);
        }
    }
    {

    }
}
