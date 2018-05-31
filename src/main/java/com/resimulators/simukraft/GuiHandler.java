package com.resimulators.simukraft;

import com.resimulators.simukraft.client.gui.*;
import com.resimulators.simukraft.common.tileentity.TileFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by fabbe on 21/01/2018 - 12:24 AM.
 */
public class GuiHandler implements IGuiHandler {
    public static final int GUI_SIM = 0;
    public static final int GUI_FARM = 1;
    public static final int GUI_MINER = 2;
    public static final int GUI_START = 3;
    public static final int GUI_HIRED = 4;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x,y,z));
        if (ID == GUI_SIM)
            return new GuiSim(player);
        if (ID == GUI_FARM)
            return new GuiFarm((TileFarm)tileEntity);
        if (ID == GUI_MINER)
            return new GuiMiner();
        if (ID == GUI_START)
            return new GuiStart();
        if (ID == GUI_HIRED)
            return new GuiHire((TileFarm) tileEntity);
        return null;
    }

}
