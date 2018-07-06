package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.tileentity.TileFarm;
import com.resimulators.simukraft.network.FireSimTriggerPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiFarm extends GuiScreen {
    private World world = Minecraft.getMinecraft().world;
    private int buttonWidth = 200;
    private int xOffset = 105;
    private int yOffset = 10;
    private int x = 0;
    private String status = "";
    private GuiButton firebutton;
    private GuiButton changeseed;
    private List<String> seeds = new ArrayList<>();
    private int seed = 0;
    private TileFarm tilefarm;
    private int mouseX;
    private int mouseY;

    public GuiFarm(TileFarm tileFarm) {
        this.tilefarm = tileFarm;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        fontRenderer.drawString("Selected seed", width / 2 - fontRenderer.getStringWidth("Selected seed") / 2, height / 2, Color.white.getRGB());
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void initGui() {
        buttonList.clear();
        seeds.clear();
        seeds.add("Wheat");
        seeds.add("Carrot");
        seeds.add("Potato");
        seed = tilefarm.getSeed();
        buttonList.add(changeseed = new GuiButton(0, width / 2 - 100, height / 2, seeds.get(seed)));
        buttonList.add(firebutton = new GuiButton(1, width / 2 - 100, (height / 4) * 3, "Fire Sim"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                if (seed + 1 > seeds.size() - 1) {
                    seed = 0;
                } else {
                    seed++;
                }
            case 1:
                tilefarm.setHired(false);
                tilefarm.setId(null);
                PacketHandler.INSTANCE.sendToServer(new FireSimTriggerPacket(tilefarm.getPos().getX(), tilefarm.getPos().getY(), tilefarm.getPos().getZ()));
                mc.displayGuiScreen(null);
                changeseed.displayString = seeds.get(seed);
        }

    }

    @Override
    public void onGuiClosed() {
        tilefarm.setSeed(seed);
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

