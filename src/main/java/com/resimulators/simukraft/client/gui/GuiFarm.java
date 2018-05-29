package com.resimulators.simukraft.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.io.IOException;

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
    private int mouseX;
    private int mouseY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        if (!status.equals("hiring")) {
            drawString(mc.fontRenderer, "Farmer", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public void initGui() {
        buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height - 90, "Hire"));
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    private void updateButtons() {
        if (status.equals("hiring")) {
            button1.visible = false;
            button1.enabled = false;
        }

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

