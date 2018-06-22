package com.resimulators.simukraft.common.tileentity.Events;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TileEntityCreate {
    @SubscribeEvent
    public void checkBlock(BlockEvent.PlaceEvent event)
    {
        if (event.getPlacedBlock().getBlock().hasTileEntity(event.getPlacedBlock()));
            {
                BlockPos pos = new BlockPos(event.getPos());
                if (event.getWorld().getTileEntity(pos) instanceof iSimJob){
                SaveSimData.get(event.getWorld()).addTile(event.getWorld().getTileEntity(pos));
            }
        }
    }
}
