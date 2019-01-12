package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiMiner extends GuiScreen {
    TileMiner miner;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private int mouseX;
    private int mouseY;
    private int buttoni;
    private int mode;
    private int widths;
    private int heights;
    private int depths;
    private GuiTextField heightbox;
    private GuiTextField depthbox;
    private GuiTextField widthbox;


    public GuiMiner(TileMiner miner){
        this.miner = miner;
        mode = miner.getMode();
        widths = miner.getWidth();
        heights = miner.getHeight();
        depths = miner.getDepth();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (mode == 0){
            mc.fontRenderer.drawString("Quarry Mode",width/4-mc.fontRenderer.getStringWidth("Quarry Mode")/2,height - 100,Color.WHITE.getRGB());
            if (!heightbox.getVisible()) heightbox.setVisible(true);// Makes the height changeable
        }
        if (mode == 1){
            mc.fontRenderer.drawString("StripMine Mode",width/4-mc.fontRenderer.getStringWidth("StripMine Mode")/2,height - 100,Color.WHITE.getRGB());
            if (!depthbox.getVisible())heightbox.setVisible(true);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.add(button1 = new GuiButton(0,width/4-100,height - 80,"Change Mode"));
        heightbox = new GuiTextField(1,mc.fontRenderer,width/4*3-50,height -80,50,20);
        depthbox = new GuiTextField(2,mc.fontRenderer,width/4*3+50,height-80,50,20);
        widthbox = new GuiTextField(2,mc.fontRenderer,width/4*3,height-80,50,20);
        if (mode == 0){//Quarry mode
            heightbox.setVisible(false);
        }
        if (mode == 1){//StripMine Mode
            depthbox.setVisible(false);
        }
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                if (mode == 0){ mode = 1;
                break;}
                if (mode == 1) mode = 0;
                break;
        }
        super.actionPerformed(button);
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}