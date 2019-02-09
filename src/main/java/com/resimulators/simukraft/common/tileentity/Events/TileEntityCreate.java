package com.resimulators.simukraft.common.tileentity.Events;

import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.TileCattle;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TileEntityCreate {
    @SubscribeEvent
    public void checkBlock(BlockEvent.PlaceEvent event) {
        if (event.getPlacedBlock().getBlock().hasTileEntity(event.getPlacedBlock())) {
            BlockPos pos = new BlockPos(event.getPos());
            if (event.getWorld().getTileEntity(pos) instanceof ISimJob || event.getWorld().getTileEntity(pos) instanceof TileCattle) {

                long factionid = event.getPlayer().getCapability(ModCapabilities.PlayerCap,null).getfactionid();
                SaveSimData.get(event.getWorld()).getfaction(factionid).addJobBlock(event.getWorld().getTileEntity(pos));

            }
        }
    }
}