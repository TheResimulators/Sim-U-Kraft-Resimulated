package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiMiner extends GuiScreen {
    String hiredSim = "";
    
    private int buttonWidth = 200;
    private int screen_width = 1080;
    private int xOffset = 105;
    private int yOffset = 0;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private List<EntitySim> sims;
    private int mouseX;
    private int mouseY;

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
        yOffset = 10;
        sims = SimToHire.sims;
        buttonList.clear();
        if (status.equals("hiring")) {
        } else {
            buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height - 80, "Hire"));
        }
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
        if (status.equals("hiring")){
        for (int i = 0; i < sims.size(); i++){
            x++;
            if (((x * xOffset) + 30) > width){
                yOffset += 25;
                x = 1;

            }
            System.out.println(width + "," + x*xOffset+45);
            String name = sims.get(i).getName();
            buttonList.add(new GuiButton(i, x * xOffset-75, yOffset + 5,100,20, name + " last name"));
            }

            }


        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                status = "hiring";
                updateButtons();
                break;
            case 1:
                break;
            default:
                disableButton(button.id);
        }
        super.actionPerformed(button);
    }

    private void updateButtons(){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;
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





