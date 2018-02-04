package com.resimulators.simukraft;

import com.resimulators.simukraft.client.gui.GuiSim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by fabbe on 21/01/2018 - 12:24 AM.
 */
public class GuiHandler implements IGuiHandler {
    public static final int GUI_SIM = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SIM) {
            return new GuiSim(player);
        }

        return null;
    }
}
