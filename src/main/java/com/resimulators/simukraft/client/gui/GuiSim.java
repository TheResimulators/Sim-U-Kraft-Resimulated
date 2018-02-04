package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * Created by fabbe on 21/01/2018 - 12:26 AM.
 */
public class GuiSim extends GuiScreen {
    private EntitySim entitySim;
    private String simName;

    public GuiSim(EntityPlayer player) {
        try {
            int ID = 0;
            for (Entity e : player.world.getLoadedEntityList()) {
                if (player.getTags().contains("ID" + e.getEntityId())) {
                    ID = e.getEntityId();
                    break;
                }
            }
            entitySim = (EntitySim)player.getEntityWorld().getEntityByID(ID);
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
            TextureManager manager = mc.getTextureManager();
            manager.bindTexture(new ResourceLocation("textures/gui/demo_background.png"));

            GlStateManager.disableLighting();
            drawString(mc.fontRenderer, simName, (width / 2 - 125 + 8), (height / 2 - 100 + 8), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Gender: " + entitySim.getGender(), (width / 2 - 125 + 8), (height / 2 - 100 + 20), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Grown up: " + (entitySim.getGrowingAge() == 1 ? "true" : "false"), (width / 2 - 125 + 8), (height / 2 - 100 + 32), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Variation: " + entitySim.getVariation(), (width / 2 - 125 + 8), (height / 2 - 100 + 44), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Profession: " + entitySim.getLabeledProfession(), (width / 2 - 125 + 8), (height / 2 - 100 + 56), Color.WHITE.getRGB());
            drawString(mc.fontRenderer, "Building: " + entitySim.isAllowedToBuild(), (width / 2 - 125 + 8), (height / 2 - 100 + 80), Color.WHITE.getRGB());
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
        super.initGui();
    }
}
