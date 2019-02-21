package com.resimulators.simukraft;

import com.resimulators.simukraft.client.gui.*;
import com.resimulators.simukraft.common.containers.SimContainer;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.TileCattle;
import com.resimulators.simukraft.common.tileentity.TileFarm;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import com.resimulators.simukraft.common.tileentity.TileSheep;
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

    public enum GUI {
        SIM, FARM, MINER, START, HIRED, SIMINV, BUILDER, CATTLE, SHEEP
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI.SIMINV.ordinal()) {
            return new SimContainer(player.inventory, (EntitySim) world.getEntityByID(x));
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        if (ID == GUI.SIM.ordinal())
            return new GuiSim(player);
        else if (ID == GUI.FARM.ordinal())
            return new GuiFarm((TileFarm) tileEntity);
        else if (ID == GUI.MINER.ordinal())
            return new GuiMiner((TileMiner) tileEntity);
        else if (ID == GUI.START.ordinal())
            return new GuiStart();
        else if (ID == GUI.HIRED.ordinal())
            return new GuiHire(tileEntity);
        else if (ID == GUI.SIM.ordinal())
            return new GuiSim(player);
        else if (ID == GUI.SIMINV.ordinal())
            return new GuiSimInv(player.inventory, (EntitySim) world.getEntityByID(x));
        else if (ID == GUI.BUILDER.ordinal())
            return new GuiBuilding(x, z, y);
        else if (ID == GUI.CATTLE.ordinal())
            return new GuiCattle((TileCattle) tileEntity);
        else if (ID == GUI.SHEEP.ordinal())
            return new GuiSheep((TileSheep) tileEntity);
        return null;
    }
}
