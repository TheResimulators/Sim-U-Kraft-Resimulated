package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GuiFarm extends GuiScreen {
    boolean hiredSim = false;
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
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
        if (!status.equals("hiring"))
        {
            buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height - 90, "Hire"));
            button1.visible = true;
            button2.enabled = true;
        }
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
        x = 160;
        int y = 15;
        int i = 0;
        Set<UUID> unemployed_sims = SimEventHandler.getUnemployedSims();
        System.out.println("Unemployed sims: " + unemployed_sims);
        for (UUID sims : unemployed_sims)
        {
            System.out.println(sims);
            EntitySim sim = (EntitySim) server.getEntityFromUuid(sims);
            String name = sim.getCustomNameTag();
            buttonList.add(new GuiButton(i,i*x+20,y+15,150,20,name));
            if (status.equals("hiring")) {
                buttonList.get(i).enabled = true;
                buttonList.get(i).visible = true;
            }
            else
            {
                buttonList.get(i).enabled = false;
                buttonList.get(i).visible = false;
            }
            i++;

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
        }
        super.actionPerformed(button);
    }

    private void updateButtons(int id){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;

        } }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }}

