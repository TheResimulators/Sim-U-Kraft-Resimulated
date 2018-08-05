package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Set;
import java.util.UUID;

public class HudGui extends Gui {
    private Set<UUID> sim;
    private int population = 1;
    private static float credits;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        if (SaveSimData.get(Minecraft.getMinecraft().world) != null){
                credits = SimEventHandler.getCredits();
                if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES){
                    Minecraft mc = Minecraft.getMinecraft();
                    sim = SaveSimData.get(Minecraft.getMinecraft().world).getTotalSims(SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID()));
                    if (sim == null) {
                        population = 0;
                    } else {
                        population = sim.size();
                    }
                    int unemployedsize;
                    if (SaveSimData.get(Minecraft.getMinecraft().world).getUnemployedSims(SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID())) != null){
                    unemployedsize = SaveSimData.get(Minecraft.getMinecraft().world).getUnemployedSims(SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID())).size();}
                    else{
                        unemployedsize = 0; }
                    drawString(mc.fontRenderer, "Population " + population + ", Unemployed sims: " + unemployedsize, 1, 1, Color.WHITE.getRGB());
                    drawString(mc.fontRenderer, "Credits: " + credits, 1, 11, Color.WHITE.getRGB());
                }
            }
        }
    }

