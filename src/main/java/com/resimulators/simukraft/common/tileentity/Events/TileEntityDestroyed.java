package com.resimulators.simukraft.common.tileentity.Events;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import com.resimulators.simukraft.network.FireSimPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@Mod.EventBusSubscriber
public class TileEntityDestroyed {



    @SubscribeEvent
    public static void BlockDestroyed(BlockEvent.BreakEvent event)
    {
        System.out.println("Event executed");
        if (!event.getWorld().isRemote){
        if (event.getWorld().getTileEntity(event.getPos()) instanceof iSimJob)
        {
            TileDestroy(event.getPos(),(WorldServer) event.getWorld());
            }
        }
    }
    public static void TileDestroy(BlockPos pos,WorldServer world)
    {
        System.out.println("successfilly called this method");
        iSimJob entity =(iSimJob) world.getTileEntity(pos);
        if (entity.getId() != null) {
        SaveSimData.get(world).firedSim(entity.getId());
        EntitySim sim = (EntitySim) world.getEntityFromUuid(entity.getId());
        sim.setProfession(0);
        PacketHandler.INSTANCE.sendToAll(new FireSimPacket(entity.getId(),sim.getEntityId()));
        entity.setId(null);
        }

    }
}
