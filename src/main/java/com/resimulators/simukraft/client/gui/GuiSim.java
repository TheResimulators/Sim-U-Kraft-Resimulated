package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SimInvPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;


import java.awt.*;

/**
 * Created by fabbe on 21/01/2018 - 12:26 AM.
 */
public class GuiSim extends GuiScreen {
    private EntitySim entitySim;
    private String simName;
    private GuiButton inv;

    public GuiSim(EntityPlayer player) {
        try {
            int ID = 0;
            for (Entity e : player.world.getLoadedEntityList()) {
                if (player.getTags().contains("ID" + e.getEntityId())) {
                    ID = e.getEntityId();
                    break;
                }
            }
            entitySim = (EntitySim) player.getEntityWorld().getEntityByID(ID);
            simName = entitySim.getCustomNameTag();
            player.getTags().remove("ID" + ID);
        } catch (NullPointerException e) {
            e.printStackTrace();
            simName = "Sim";
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            drawString(mc.fontRenderer, simName, (width / 2 - 125 + 8), (height / 2 - 100 + 8), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Gender: " + entitySim.getGender(), (width / 2 - 125 + 8), (height / 2 - 100 + 20), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Grown up: " + (entitySim.getGrowingAge() == 1 ? "true" : "false"), (width / 2 - 125 + 8), (height / 2 - 100 + 32), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Variation: " + entitySim.getVariation(), (width / 2 - 125 + 8), (height / 2 - 100 + 44), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Profession: " + entitySim.getLabeledProfession(), (width / 2 - 125 + 8), (height / 2 - 100 + 56), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Building: " + entitySim.isAllowedToBuild(), (width / 2 - 125 + 8), (height / 2 - 100 + 80), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Hunger: " + entitySim.getFoodLevel(), (width / 2) - 40, (height / 4) * 3 + 30, Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Health: " + entitySim.getHealth(), (width / 2) - 40, (height / 4) * 3 - 30, Color.WHITE.getRGB());
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        entitySim.setCommander(null);
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        buttonList.add(inv = new GuiButton(0, width / 2 - 100, (height / 4) * 3, "Inventory"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            PacketHandler.INSTANCE.sendToServer(new SimInvPacket(entitySim.getEntityId()));
        }
    }
}