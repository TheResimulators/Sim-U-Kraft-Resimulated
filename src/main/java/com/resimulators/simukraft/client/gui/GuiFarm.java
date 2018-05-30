package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
    private String seed = "wheat";
    private Set<String> seeds = new HashSet<>();
    private int mouseX;
    private int mouseY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private class Sim_Button extends GuiButton {
        private Sim_Button(int id, int x, int y, String string) {
            super(id, x, y, 100, 30, string);

        }
    }
}

