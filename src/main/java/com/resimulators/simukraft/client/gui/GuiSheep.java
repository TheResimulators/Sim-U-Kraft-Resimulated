package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.tileentity.TileSheep;
import com.resimulators.simukraft.network.HiringPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiSheep extends GuiScreen {
    private TileSheep tileSheep;
    private menuButton hirebutton;
    private menuButton showemployess;
    private menuButton firebutton;
    private menuButton donebutton;
    private menuButton cancelbutton;
    private menuButton confirmbutton;
    private int selectedbutton;
    List<simButton> buttons = new ArrayList<>();



    public GuiSheep(TileSheep tileSheep){
        this.tileSheep = tileSheep;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        if (mc != null){
            drawDefaultBackground();
            super.drawScreen(mouseX,mouseY,partialTicks);
            System.out.println("name " + tileSheep.getSimname());
            if (tileSheep.getSimname() != null){
                drawCenteredString(fontRenderer,"Hired Sim: " + tileSheep.getSimname(),30,30, Color.WHITE.getRGB());
            }}}


    @Override
    public void initGui(){
        buttonList.add(hirebutton = new menuButton(0,0,height-20,100,"Hire Sheep Farmer"));
        buttonList.add(showemployess = new menuButton(1,width-100,height-20,100,"Show employees"));
        buttonList.add(firebutton = new menuButton(2,5,height-20,"Fire"));
        firebutton.enabled = false;
        firebutton.visible = false;
        if (tileSheep.getHired())buttonList.get(0).enabled = false;
        buttonList.add(donebutton = new menuButton(3,0,1,"Done"));
        buttonList.add(cancelbutton = new menuButton(4,width/2,height-20,60,"Cancel"));
        buttonList.get(4).enabled = false;
        buttonList.get(4).visible = false;
        buttonList.add(confirmbutton = new menuButton(5,width/2-60,height - 20,60,"Confirm"));
        buttonList.get(5).visible = false;
        buttonList.get(5).enabled = false;
        int ypos = 30;
        int i = 6;
        int pos = 0;
        int num = 0;
        String name;

        List<String> names = (tileSheep).getnames();
        for (int id: (tileSheep).getSims())
        {
            int xpos = pos*100+20+pos*5;

            if (xpos > width - 60) {
                pos = 0;
                xpos = pos * 100 + 20;
                ypos += 25;

            }
            name = names.get(num);
            if (name == null) {
                name = "Error";
            }
            simButton button = new simButton(i,xpos,ypos,name,id);
            buttons.add(button);
            buttonList.add(button);
            button.enabled = false;
            button.visible = false;
            pos++;
            num++;

        }}

    @Override
    public void updateScreen(){

    }


    @Override
    public void actionPerformed(GuiButton button){

        switch (button.id){
            case 0:
                button.visible = false;
                button.enabled = false;
                showemployess.enabled = false;
                showemployess.visible = false;
                donebutton.visible = false;
                donebutton.enabled = false;
                confirmbutton.visible = true;
                confirmbutton.enabled = true;
                cancelbutton.visible = true;
                cancelbutton.enabled = true;
                showsimbuttons();
                break;
            case 1:
                break;

            case 2:
                break;
            case 3:
                mc.displayGuiScreen(null);
            case 4:
                mc.displayGuiScreen(null);
                break;
            case 5:
                PacketHandler.INSTANCE.sendToServer(new HiringPacket(selectedbutton,tileSheep.getProfessionID(),tileSheep.getPos().getX(),tileSheep.getPos().getY(),tileSheep.getPos().getZ()));
                tileSheep.setHired(true);
                mc.displayGuiScreen(null);
                System.out.println("get sim from world using id " + selectedbutton);
            default:
                if (button instanceof simButton){
                    resetenableduttons();
                    button.enabled = false;
                    selectedbutton = ((simButton) button).simid;
                }
        }
    }

    private void showsimbuttons(){
        System.out.println("amount of sims" + buttons.size());
        for (simButton button: buttons){
            button.visible = true;
            button.enabled = true;
        }
    }


    private void resetenableduttons(){
        for (simButton button:buttons){
            button.enabled = true;
        }
    }


    class menuButton extends GuiButton{
        menuButton(int id,int x, int y, String string) {super(id,x,y,40,20,string);}
        menuButton(int id,int x, int y,int width, String string) {super(id,x,y,width,20,string);}
    }

    class simButton extends GuiButton{
        int simid;
        simButton(int id,int x,int y, String string,int simid){super(id,x,y,100,20,string);
            this.simid = simid;}
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
