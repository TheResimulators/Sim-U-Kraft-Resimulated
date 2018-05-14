package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GuiFarm extends GuiScreen {
    boolean hiredSim = false;
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    List<EntitySim> sims;
    private int buttonWidth = 200;
    private int xOffset = 105;
    private int yOffset = 10;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;
    private int mouseX;
    private int mouseY;
    private int buttoni;

    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        if (status != "hiring") {
            drawString(mc.fontRenderer, "Farmer", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        x = 0;
        buttonList.add(button1 = new GuiButton(0,width/2-buttonWidth/2,height-90,"Hire"));
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
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
        }
        super.actionPerformed(button);
    }

    private void updateButtons(int id){
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
    }}

