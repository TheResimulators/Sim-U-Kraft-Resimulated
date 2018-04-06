package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class SimToHire {

    public static List<EntitySim> unemployedsims = new ArrayList<>();
    public static List<EntitySim> totalsims = new ArrayList<>();

    public static float getCredits() {
        //System.out.println(" getting credits that is equal to: " + credits);
        return credits;
    }
    public static float setCredits(float credit){
        credits = credit;
        //System.out.println(" set credits to" + credits);
        return credits;
    }
    public static UUID getSimID(){
        return sim;
    }
    static UUID sim;
    static float credits = 10;
    Entity name;
    NBTTagCompound compound = new NBTTagCompound();
    @SubscribeEvent
    public void availableSims(EntityJoinWorldEvent event) {
        World world = event.getEntity().world;
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            //System.out.println("Entity is a sim" + " world is " + world);
            if (!world.isRemote) {
                //System.out.println("world is not remote");
                if (!totalsims.contains(event.getEntity())) {
              //      System.out.println("adding sim");
                    name = event.getEntity();
                    unemployedsims.add((EntitySim) name);
                    totalsims.add((EntitySim) name);
                    System.out.println("added " + name + " " + event.getEntity().getPersistentID());
                    ((EntitySim) event.getEntity()).inlist = true;
                    sim = (event.getEntity().getPersistentID());
                    PacketHandler.INSTANCE.sendToAll(new Siminfo_packet(sim));
                }
            }
        }

    }
    @SubscribeEvent
    public void world_save(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event){
        System.out.println("save ");
        compound.setInteger("total_sims",totalsims.size());
        compound.setInteger("unemployed_sims",unemployedsims.size());
        for(int i = 0;i< totalsims.size();i++){
            UUID id = totalsims.get(i).getPersistentID();
                compound.setUniqueId("sim " + i,id);
                System.out.println("added sim " + id + " to nbt");

            }
        for(int i = 0;i< totalsims.size();i++){
            UUID id = totalsims.get(i).getPersistentID();
            compound.setUniqueId("sims " + i,id);
        }

        }
    @SubscribeEvent
    public void world_load(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event){
        System.out.println("loading sims");
        MinecraftServer server = event.player.getEntityWorld().getMinecraftServer();
        int total_sims = compound.getInteger("total_sims");
        int unemployed_sims = compound.getInteger("unemployed_sims");
        for (int i = 0;i<unemployed_sims;i++){
            if (compound.hasKey("sim " + i)){
                UUID sim = compound.getUniqueId("sim " + i);
                EntitySim e = (EntitySim) server.getEntityFromUuid(sim);
                if (!unemployedsims.contains(e)){
                 unemployedsims.add(e);
            }}
        }
        for (int i = unemployed_sims;i<total_sims+unemployed_sims;i++){
            if (compound.hasKey("sims " + i)){
                UUID sim = compound.getUniqueId("sims " + i);
                System.out.println("sim " + sim);
                System.out.println("sim id =" + sim + "with persistent id of " + totalsims.get(i).getPersistentID());
                EntitySim e = (EntitySim) server.getEntityFromUuid(sim);
                if (!totalsims.contains(e)){
                totalsims.add(e);
            }
        }
    }
}}
