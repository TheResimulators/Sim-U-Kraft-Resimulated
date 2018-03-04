package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerFirstJoin {
    private boolean isLoaded = false;
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void OnTick(TickEvent PlayerTickEvent, World world) {
        if (!world.isRemote && !isLoaded) {
            isLoaded = true;
            if (!world.isRemote) {
                Minecraft.getMinecraft().player.openGui(SimUKraft.instance, GuiHandler.GUI_START, world, 0, 0, 0);
            }
        }

    }
}