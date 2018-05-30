package com.resimulators.simukraft.common.entity.entitysim;
import com.jcraft.jogg.Packet;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SimDeathPacket;
import com.resimulators.simukraft.network.SimInfoPacket;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;


public class SimEventHandler {
    static SaveSimData data;
    public static float getCredits() {
        //System.out.println(" getting credits that is equal to: " + credits);
        return credits;
    }
    public static float setCredits(float credit){
        credits = credit;
        //System.out.println(" set credits to" + credits);
        return credits;
    }
    private static float credits = 10;
    public static void setWorldSimData(SaveSimData datas) {

        data = datas;
    }


    public static SaveSimData getWorldSimData() {

        return data;
    }

    @SubscribeEvent
    public void Sim_Spawn(LivingSpawnEvent event)
    {
        World world = event.getWorld();
        data = SaveSimData.get(world);

        if (event.getEntity() instanceof EntitySim){

            if (!world.isRemote)
            {

            if (!data.getTotalSims().contains(event.getEntity().getPersistentID()))
            {
                UUID id = event.getEntity().getPersistentID();
                data.spawnedSim(id);

                PacketHandler.INSTANCE.sendToAll(new SimInfoPacket(id));
            }
        }
    }
}

    @SubscribeEvent
    public void Sim_Death(LivingDeathEvent event){


        if (event.getEntity() instanceof EntitySim){
            EntitySim sim = (EntitySim) event.getEntity();
            World world = sim.world;
            if (!world.isRemote)
            {
                UUID id = event.getEntity().getPersistentID();
                data.simDied(id);
                PacketHandler.INSTANCE.sendToAll(new SimDeathPacket(id));
            }

        }
    }
}