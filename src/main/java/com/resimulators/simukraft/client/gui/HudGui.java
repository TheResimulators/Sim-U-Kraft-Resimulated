package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HudGui  extends Gui {
    private Set<UUID> sim;
    private int population = 1;
    private static float credits;


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        credits = SimEventHandler.getCredits();
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            Minecraft mc = Minecraft.getMinecraft();
            if (SimEventHandler.getWorldSimData() != null) {
                sim = SimEventHandler.getWorldSimData().getTotalSims();
            if (sim == null){
                population = 0;
            }else {
                population = sim.size();
            }

                drawString(mc.fontRenderer, "Population " + population + " ,Unemployed sims: " + SimEventHandler.getWorldSimData().getUnemployed_sims().size() , 1, 1, Color.WHITE.getRGB());
                drawString(mc.fontRenderer,"Credits: " + credits, 1, 11, Color.WHITE.getRGB());
        }
    }}
}
