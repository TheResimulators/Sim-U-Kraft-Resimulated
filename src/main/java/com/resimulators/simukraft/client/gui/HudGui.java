package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;

public class HudGui  extends Gui {
    private List<EntitySim> sims;
    private int population = 1;
    private int credits = 10;
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            Minecraft mc = Minecraft.getMinecraft();
                drawString(mc.fontRenderer, "Population: " + population + " Credits: " + credits, 1, 1, Color.WHITE.getRGB());
            
        }
    }
}
