package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HudGui extends Gui {
    private List<UUID> sim = new ArrayList<>();
    private int population = 1;
    private static float credits;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo)
            return;
        SaveSimData simData = SaveSimData.get(mc.world);
        if (sim == null)
            return;
        PlayerCapability playerCap = mc.player.getCapability(ModCapabilities.getPlayerCap(), null);
        if (playerCap == null)
            return;

        int mode = playerCap.getmode();
        long factionid = playerCap.getfactionid();
        List<UUID> sims = simData.getfaction(factionid).getTotalSims();
        int population = sims == null ? 0 : sims.size();
        sims = simData.getfaction(factionid).getUnemployedSims();
        int unemployedsize = sim == null ? 0 : sims.size();

        drawString(mc.fontRenderer, "Population " + population + ", Unemployed sims: " + unemployedsize, 1, 1, Color.WHITE.getRGB());
        if (mode == 0)
            drawString(mc.fontRenderer, "Credits: " + SimEventHandler.getCredits(), 1, 11, Color.WHITE.getRGB());
    }
    }

