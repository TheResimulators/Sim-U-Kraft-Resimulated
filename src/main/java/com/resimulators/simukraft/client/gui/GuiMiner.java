package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiMiner extends GuiScreen {
    String hiredSim = "";

    private int buttonWidth = 200;
    private int xOffset = 10;
    private int yOffset = 0;
    private String status;
    private GuiButton button1;
    private GuiButton button2;
    private List<EntitySim> sims;

    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        drawString(mc.fontRenderer, "Miner", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        sims = SimToHire.sims;
        buttonList.clear();
        for (int i = 2; i < sims.size(); i++) {
            String name = sims.get(i).getName();
            buttonList.add(new GuiButton(i, i * xOffset, yOffset, name));
            if (i > 5) {
                yOffset = 40;
            }
        }
        buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height / 4, "Hire"));
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height / 4 + (4 * yOffset), "Cancel"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                status = "hiring";
                updateButtons();
                break;
        }
        super.actionPerformed(button);
    }

    private void updateButtons(){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}





