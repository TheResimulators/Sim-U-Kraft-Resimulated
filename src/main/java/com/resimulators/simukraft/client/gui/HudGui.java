package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HudGui extends Gui {
    private List<UUID> sim = new ArrayList<UUID>();
    private int population = 1;
    private static float credits;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        if (SaveSimData.get(Minecraft.getMinecraft().world) != null){
                credits = SimEventHandler.getCredits();
                if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
                    if (Minecraft.getMinecraft().gameSettings.showDebugInfo)return;
                    int mode = Minecraft.getMinecraft().player.getCapability(ModCapabilities.getPlayerCap(),null).getmode();
                    if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.getPlayerCap(),null) == null){
                        mode = -1;
                    }
                    if (mode != -1){
                    Minecraft mc = Minecraft.getMinecraft();
                    long factionid = Minecraft.getMinecraft().player.getCapability(ModCapabilities.getPlayerCap(),null).getfactionid();
                    sim = SaveSimData.get(Minecraft.getMinecraft().world).getfaction(factionid).getTotalSims();
                    if (sim == null) {
                        population = 0;
                    } else {
                        population = sim.size();
                    }
                    int unemployedsize;
                    if (SaveSimData.get(Minecraft.getMinecraft().world).getfaction(factionid).getUnemployedSims() != null){
                    unemployedsize = SaveSimData.get(Minecraft.getMinecraft().world).getfaction(factionid).getUnemployedSims().size();}
                    else{
                        unemployedsize = 0;}
                    drawString(mc.fontRenderer, "Population " + population + ", Unemployed sims: " + unemployedsize, 1, 1, Color.WHITE.getRGB());
                    if (mode == 0){
                    drawString(mc.fontRenderer, "Credits: " + credits, 1, 11, Color.WHITE.getRGB());
                        }
                    }
                }
            }
        }
    }

