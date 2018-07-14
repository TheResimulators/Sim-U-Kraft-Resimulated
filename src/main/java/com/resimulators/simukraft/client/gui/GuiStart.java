package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.network.ModeChangePacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class GuiStart extends GuiScreen {
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private int Gamemode = -1;
    private int button_width = 100;
    private int button_height = 20;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawString(fontRenderer, "Welcome to Sim-u-Kraft", width / 2 - button_width / 2, height / 3 - 50, Color.WHITE.getRGB());
        drawCenteredString(fontRenderer, "Please choose a mode to play in:", width / 2, height / 3 - 30, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        int buttonx = button1.x;
        int buttony = button1.y;
        String desc;
        desc = "Have to supply most resources. " + "Money needed to build and hire sims etc.";
        //System.out.println(buttonx+","+ buttony+","+(buttonx+button_width)+","+(buttony-button_height)+","+mouseX+","+mouseY);
        if (buttonx <= mouseX && mouseX <= (buttonx + button_width)) {
            if (buttony <= mouseY && mouseY <= (buttony + button_height)) {
                drawHoveringText(desc, buttonx - fontRenderer.getStringWidth(desc) / 3, buttony - 10);
            }
        }
        buttony = button2.y;
        buttonx = button2.x;
        desc = "Have to supply all resources. " + "Money needed to build and hire sims etc.";
        if (buttonx <= mouseX && mouseX <= (buttonx + button_width)) {
            if (buttony <= mouseY && mouseY <= (buttony + button_height)) {
                drawHoveringText(desc, buttonx - fontRenderer.getStringWidth(desc) / 3, buttony - 10);
            }
        }
        buttonx = button3.x;
        buttony = button3.y;
        desc = "Have to supply no resources. " + "Everything is Free. Let your imagination run wild";
        if (buttonx <= mouseX && mouseX <= (buttonx + button_width)) {
            if (buttony <= mouseY && mouseY <= (buttony + button_height)) {
                drawHoveringText(desc, buttonx - fontRenderer.getStringWidth(desc) / 4, buttony - 10);
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(button1 = new GuiButton(1, width / 2 - button_width / 2, height / 3 - 10, button_width, button_height, "Survival"));
        buttonList.add(button2 = new GuiButton(2, width / 2 - button_width / 2, height / 3 + 40, button_width, button_height, "Hardcore"));
        buttonList.add(button3 = new GuiButton(3, width / 2 - button_width / 2, height / 3 + 90, button_width, button_height, "Creative"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:{
                Gamemode = 0;
            }
            case 2:{
                Gamemode = 1;
            }
            case 3: {
                Gamemode = -1;
            }
        }
        PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(Minecraft.getMinecraft().player.getUniqueID(),Gamemode));

        Minecraft.getMinecraft().displayGuiScreen(null);
        super.actionPerformed(button);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}

