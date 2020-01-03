package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.network.ClientStructuresPacket;
import com.resimulators.simukraft.network.LoadStructurePacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiBuilding extends GuiScreen {
    private categoryButton commercialButton;
    private categoryButton industrialButton;
    private categoryButton residentialButton;
    private categoryButton customButton;
    private List<structureButton> industrialbuttons = new ArrayList<>();
    private List<structureButton> residentialbuttons = new ArrayList<>();
    private List<structureButton> commercialbuttons = new ArrayList<>();
    private List<structureButton> custombuttons = new ArrayList<>();
    private List<String> residential = new ArrayList<>();
    private List<String> commercial = new ArrayList<>();
    private List<String> industrial = new ArrayList<>();
    private List<String> special = new ArrayList<>();
    private List<ClientStructuresPacket.StructureInfo> structureInfos;
    private int xpos;
    private int ypos;
    private int zpos;
    private TileConstructor constructor;

    public GuiBuilding(int xpos, int zpos, int ypos, TileConstructor constructor){
        this.xpos = xpos;
        this.zpos = zpos;
        this.ypos = ypos;
        this.constructor = constructor;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {

        super.drawScreen(mouseX,mouseY,partialTicks);
    }


    @Override
    public void initGui()
    {
        buttonList.add(residentialButton = new categoryButton(0,width/2-108,height/2-5,"Residential"));
        buttonList.add(industrialButton = new categoryButton(1,width/2+8,height/2-5,"Industrial"));
        buttonList.add(commercialButton = new categoryButton(2,width/2-108,height/2+15,"Commercial"));
        buttonList.add(customButton = new categoryButton(3,width/2+8,height/2+15,"Custom"));
        addbuttons();
        super.initGui();
    }


    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch(button.id){

            case 0:
                residentialButton.enabled = false;
                residentialButton.visible = false;
                industrialButton.enabled = false;
                industrialButton.visible = false;
                commercialButton.enabled = false;
                commercialButton.visible = false;
                customButton.enabled = false;
                customButton.visible = false;
                updatebuttons(residentialbuttons);
                break;

            case 1:{
                residentialButton.enabled = false;
                residentialButton.visible = false;
                industrialButton.enabled = false;
                industrialButton.visible = false;
                commercialButton.enabled = false;
                commercialButton.visible = false;
                customButton.enabled = false;
                customButton.visible = false;
                updatebuttons(industrialbuttons);
                break;
        }
            case 2: {
                residentialButton.enabled = false;
                residentialButton.visible = false;
                industrialButton.enabled = false;
                industrialButton.visible = false;
                commercialButton.enabled = false;
                commercialButton.visible = false;
                customButton.enabled = false;
                customButton.visible = false;
                updatebuttons(commercialbuttons);
                break;
            }
            case 3: {
                residentialButton.enabled = false;
                residentialButton.visible = false;
                industrialButton.enabled = false;
                industrialButton.visible = false;
                commercialButton.enabled = false;
                commercialButton.visible = false;
                customButton.enabled = false;
                customButton.visible = false;
                updatebuttons(custombuttons);
                break;
            }


            default:
                if (button instanceof structureButton){
                    PacketHandler.INSTANCE.sendToServer(new LoadStructurePacket(button.displayString,((structureButton) button).type,xpos,ypos,zpos));
                    mc.displayGuiScreen(null);
                    break;
                }
        }
    }


    public void setstructures(List<String> industrial, List<String> commercial, List<String> residential, List<String> special, List<ClientStructuresPacket.StructureInfo> structureInfos){
        this.industrial = industrial;
        this.commercial = commercial;
        this.residential = residential;
        this.special = special;
        this.structureInfos = structureInfos;
        addbuttons();
    }


    private void addbuttons(){
        int i = 4;
        int x = 85;
        int xi = 0;
        int y = 30;
        for (String string:this.industrial){
            if (x *xi >= width-100){
                y += 30;
                xi = 0;
            }
            buttonList.add(new structureButton(i,x*xi + 10,y,string.replace(".nbt",""),"industrial"));
            buttonList.get(buttonList.size()-1).enabled = false;
            buttonList.get(buttonList.size()-1).visible = false;
            industrialbuttons.add((structureButton) buttonList.get(buttonList.size()-1));
            xi++;
            i++;
        }
        x = 85;
         xi = 0;
         y = 30;

        for (String string:this.residential){
            if (x *xi >= width-100){
                y += 30;
                xi = 0;
            }
            buttonList.add(new structureButton(i,x*xi + 10,y,string.replace(".struct",""),"residential"));
            buttonList.get(buttonList.size()-1).enabled = false;
            buttonList.get(buttonList.size()-1).visible = false;
           residentialbuttons.add((structureButton) buttonList.get(buttonList.size()-1));
            xi++;
            i++;
        }
        x = 85;
        xi = 0;
        y = 30;
        for (String string:this.commercial){
            if (x *xi >= width-100){
                y += 30;
                xi = 0;
            }
            buttonList.add(new structureButton(i,x*xi + 10,y,string.replace(".struct",""),"commercial"));
            buttonList.get(buttonList.size()-1).enabled = false;
            buttonList.get(buttonList.size()-1).visible = false;
            commercialbuttons.add((structureButton) buttonList.get(buttonList.size()-1));
            xi++;
            i++;
        }
        x = 85;
        xi = 0;
        y = 30;
        for (String string:this.special){
            if (x *xi >= width-100){
                y += 30;
                xi = 0;
            }
            buttonList.add(new structureButton(i,x*xi + 10,y,string.replace(".struct",""),"special"));
            buttonList.get(buttonList.size()-1).enabled = false;
            buttonList.get(buttonList.size()-1).visible = false;
            custombuttons.add((structureButton) buttonList.get(buttonList.size()-1));
            xi++;
            i++;
        }

    }

    private void updatebuttons(List<structureButton> buttons){
        for (structureButton button:buttons){
            System.out.println("structure buttons " + button.id);
            button.enabled = true;
            button.visible = true;
        }

    }

private class categoryButton extends GuiButton {
    private categoryButton(int id, int x, int y, String string)
    {
        super(id,x,y,120,20,string);
    }
}

private class structureButton extends  GuiButton {
    String type;
    private structureButton(int id, int x,int y,String string,String type){

        super(id,x,y,80,20,string);
        this.type = type;
    }

}
}