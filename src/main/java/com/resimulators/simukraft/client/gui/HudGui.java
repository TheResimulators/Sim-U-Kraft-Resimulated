package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;

public class HudGui  extends Gui {
    private List<EntitySim> sim;
    private int population = 1;
    private static float credits;


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        credits = SimToHire.getCredits();
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            Minecraft mc = Minecraft.getMinecraft();
            sim = SimToHire.totalsim;
            if (sim == null){
                population = 0;
            }else {
                population = sim.size();
            }

                drawString(mc.fontRenderer, "Population: " + population, 1, 1, Color.WHITE.getRGB());
                drawString(mc.fontRenderer,"Credits: " + credits, 1, 11, Color.WHITE.getRGB());
        }
    }
}
