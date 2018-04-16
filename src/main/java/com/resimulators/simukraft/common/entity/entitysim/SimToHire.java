package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SimToHire {

    public static List<EntitySim> unemployedsims = new ArrayList<>();
    public static List<EntitySim> totalsims = new ArrayList<>();
    NBTTagList unemployes_sims = new NBTTagList();
    NBTTagList total_sims = new NBTTagList();
    public static float getCredits() {
        //System.out.println(" getting credits that is equal to: " + credits);
        return credits;
    }
    public static float setCredits(float credit){
        credits = credit;
        //System.out.println(" set credits to" + credits);
        return credits;
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
        World world = event.player.getEntityWorld();

        if (!world.isRemote){
        System.out.println("save ");
        for(int i = 0;i< unemployedsims.size();i++){
            UUID id = unemployedsims.get(i).getPersistentID();
                compound.setUniqueId("sim " + i,id);
                unemployes_sims.appendTag(new NBTTagString(id.toString()));
                System.out.println("added sim " + id + " to nbt");

            }
        for(int i = 0;i< totalsims.size();i++){
            UUID id = totalsims.get(i).getPersistentID();
            compound.setUniqueId("sims " + i,id);
            total_sims.appendTag(new NBTTagString(id.toString()));
        }}
        }
    @SubscribeEvent
    public void world_load(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event){
        World world = event.player.getServer().getEntityWorld();
        if (!world.isRemote){
        System.out.println("loading sims");
        MinecraftServer server = event.player.getEntityWorld().getMinecraftServer();
        for (int i = 0;i< unemployes_sims.tagCount(); i++){
            String uuid = unemployes_sims.getStringTagAt(i);
            UUID sim = UUID.fromString(uuid);
            EntitySim e =(EntitySim) server.getEntityFromUuid(sim);
            if (!unemployedsims.contains(e)){
            unemployedsims.add(e);
        }}
            for (int i = 0;i< total_sims.tagCount(); i++){
                String uuid = unemployes_sims.getStringTagAt(i);
                UUID sim = UUID.fromString(uuid);
                EntitySim e =(EntitySim) server.getEntityFromUuid(sim);
                if (!totalsims.contains(e)){
                   totalsims.add(e);
                }}
        }
    }
}

