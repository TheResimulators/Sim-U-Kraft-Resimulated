package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.*;


public class SimEventHandler {
    private static Set<UUID> total_sims = new HashSet<>();
    private static Set<UUID> unemployed_sims = new HashSet<>();
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


    public static Set<UUID> getUnemployedSims()
    {
        return unemployed_sims;
    }

    public static SaveSimData getWorldSimData() {
        System.out.println("Is data null? :"+data);
        return data;
    }


    public static void simFired(UUID id) {
        unemployed_sims.add(id);
        getWorldSimData().firedSim(id);
    }

    public static void simSpawned(UUID id){
        total_sims.add(id);
        unemployed_sims.add(id);
        getWorldSimData().spawnedSim(id);
    }

    public static void addTotalSim(UUID id)
    {
        total_sims.add(id);
        System.out.println("Printing data instance " + getWorldSimData());
        getWorldSimData().spawnedSim(id);
    }

    public static void addUnemployedSim(UUID id)
    {
        unemployed_sims.add(id);
        getWorldSimData().firedSim(id);
    }


    public static void simDied(UUID id)
    {
     unemployed_sims.remove(id);
     unemployed_sims.remove(id);
     getWorldSimData().simDied(id);
    }

    public static Set<UUID> getTotal_sims() {
        return total_sims;
    }

    public static void hireSims(UUID id){
        unemployed_sims.remove(id);
        data.hiredsim(id);
    }
    @SubscribeEvent
    public void Sim_Spawn(LivingSpawnEvent event)
    {
        World world = event.getWorld();
        data = SaveSimData.get(world);

        if (event.getEntity() instanceof EntitySim){

            if (!world.isRemote)
            {

            if (!getTotal_sims().contains(event.getEntity().getPersistentID()))
            {
                System.out.println("total sims do not contain sim " + event.getEntity().getPersistentID() );
                UUID id = event.getEntity().getPersistentID();
                System.out.println("Total sims:" + getTotal_sims().size());
                simSpawned(id);
                PacketHandler.INSTANCE.sendToAll(new Siminfo_packet(id));
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
                simDied(id);

            }

        }
    }
}