package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.tools.nsc.transform.patmat.Logic;

import java.beans.EventHandler;
import java.util.*;


public class SimEventHandler {
    private static Set<UUID> total_sims = new HashSet<>();
    private static Set<UUID> unemployed_sims = new HashSet<>();
    private static SaveSimData data;
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


    public static Set<UUID> getUnemployedSims()
    {
        return unemployed_sims;
    }

    public static SaveSimData getWorldSimData(){
        return data;
    }


    public static void simFired(UUID id,SaveSimData data) {
        unemployed_sims.add(id);
        data.firedSim(id);
    }

    public static void simSpawned(UUID id, SaveSimData data){
        total_sims.add(id);
        unemployed_sims.add(id);
        data.spawnedSim(id);
    }

    public static void addTotalSim(UUID id, SaveSimData data)
    {
        total_sims.add(id);
        data.addSim(id);
    }

    public static void addUnemployedSim(UUID id,SaveSimData data)
    {
        unemployed_sims.add(id);
        data.firedSim(id);
    }


    public static void simDied(UUID id, SaveSimData data)
    {
     unemployed_sims.remove(id);
     unemployed_sims.remove(id);
     data.simDied(id);
    }

    public static Set<UUID> getTotal_sims() {
        return total_sims;
    }

    public static void hireSims(UUID id, SaveSimData data){
        unemployed_sims.remove(id);
        data.hiredsim(id);
    }
    @SubscribeEvent
    public void Sim_Spawn(EntityJoinWorldEvent event)
    {

        World world = event.getWorld();
        SaveSimData data = com.resimulators.simukraft.common.entity.player.SaveSimData.get(world);
        if (event.getEntity() instanceof EntitySim){
            if (!world.isRemote)
            {
            if (!getTotal_sims().contains(event.getEntity().getPersistentID()))
            {
                System.out.println("total sims do not contain sim " + event.getEntity().getPersistentID() );
                UUID id = event.getEntity().getPersistentID();
                System.out.println("Total sims:" + getTotal_sims().size());
                simSpawned(id,data);
                PacketHandler.INSTANCE.sendToAll(new Siminfo_packet(id));
            }
        }
    }
}

    @SubscribeEvent
    public void Sim_Death(LivingDeathEvent event){
        World world = event.getEntity().getEntityWorld();
        SaveSimData data = com.resimulators.simukraft.common.entity.player.SaveSimData.get(world);

        if (event.getEntity() instanceof EntitySim){
            if (!world.isRemote)
            {
                UUID id = event.getEntity().getPersistentID();
                simDied(id,data);

            }

        }
    }
}