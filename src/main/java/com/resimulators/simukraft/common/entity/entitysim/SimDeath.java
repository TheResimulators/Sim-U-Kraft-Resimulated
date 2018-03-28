package com.resimulators.simukraft.common.entity.entitysim;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SimDeath_packet;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class SimDeath {
    UUID id;

    @SubscribeEvent
    public void SimKilled(LivingDeathEvent event){
        World world = event.getEntity().world;
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            if (!world.isRemote) {
                System.out.println("world is not remote removing sim: " + event.getEntity());
                //This should always be true unless it has already been removed
                if (SimToHire.totalsims.contains(event.getEntity())) {
                    SimToHire.totalsims.remove(event.getEntity());
                    //checks to see if the sim was unemployed
                if (SimToHire.unemployedsims.contains(event.getEntity()));{
                    SimToHire.unemployedsims.remove(event.getEntity());

                }
                    id = event.getEntity().getPersistentID();
                    System.out.println("sending dead sim to client with id:" + id);
                    PacketHandler.INSTANCE.sendToAll(new SimDeath_packet(id));}
            }
        }
    }

}
