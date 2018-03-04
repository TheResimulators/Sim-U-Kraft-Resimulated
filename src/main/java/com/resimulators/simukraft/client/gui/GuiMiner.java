package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiMiner extends GuiScreen {
    boolean hiredSim = false;
    
    private int buttonWidth = 200;
    private int xOffset = 105;
    private int yOffset = 10;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;
    private List<EntitySim> sims;
    private int mouseX;
    private int mouseY;
    private int buttoni;

    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        if (status != "hiring") {
            drawString(mc.fontRenderer, "Miner", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        x = 0;

        sims = SimToHire.sims;
        buttonList.clear();
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
        if (status.equals("hiring")) {

            buttonList.add(button4 = new GuiButton(3, width-50,30,40,20, "UP"));
            buttonList.add(button3 = new GuiButton(2, width - 50,height - 20,40,20,"Down"));
        } else {
            buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height - 80, "Hire"));
        }


        button1.enabled = !hiredSim;
        //System.out.println("status equals " +status);
        if (status.equals("hiring")){
            System.out.println(sims.size());
        for (int i = 1; i < sims.size(); i++){

            buttoni = i;
            x++;
            if (((x * xOffset) + 30) > width){
                yOffset += 25;
                x = 1;
            }
            //System.out.println(height-70 + "," + yOffset + 10);
            String name = sims.get(i).getName();
            //System.out.println("creating button " + i);
            buttonList.add(new GuiButton(i, x * xOffset-75, yOffset + 5,100,20, name + " last name"));
            //System.out.println("added button " + i );
            if (yOffset + 10 >= height-70 || yOffset + 5 <= 0){
                buttonList.get(buttoni).enabled = false;
                buttonList.get(buttoni).visible = false;
                button3.enabled = true;
                button3.visible = true;
                System.out.println("disabling button " + buttoni);
            } else {
                buttonList.get(buttoni).enabled = true;
                buttonList.get(buttoni).visible = true;
            }
            }

            }


        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                status = "hiring";
                System.out.println("status updated");
                updateButtons(button.id);
                break;
            case 1:
                this.mc.displayGuiScreen(null);
                break;
            case 2:
                yOffset += 25;
                updateButtons(button.id);
                break;
            case 3:
                yOffset += -25;
                updateButtons(button.id);
                break;
            default:
                    disableButton(button.id);
                    hiredSim = true;
                    updateButtons(button.id);



        }
        super.actionPerformed(button);
    }

    private void updateButtons(int id){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;
        if (id == buttoni){
            hiredSim = true;
            }


            initGui();
        }
    }

    private boolean disableButton(int id) {
        buttonList.get(id).enabled = false;
        buttonList.get(id).visible = false;
        return buttonList.get(id).enabled && buttonList.get(id).visible;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public class ButtonList extends GuiScrollingList {
        public ButtonList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
            super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        }

        @Override
        protected int getSize() {
            return 20;
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {

        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

        }
    }
}





