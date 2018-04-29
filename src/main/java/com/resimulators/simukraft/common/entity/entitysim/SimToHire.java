package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SimToHire {

    public static List<UUID> unemployedsims = new ArrayList<>();
    public static List<UUID> totalsims = new ArrayList<>();
    public static List<EntitySim> totalsim = new ArrayList<>();
    public static List<EntitySim> unemployedsim = new ArrayList<>();
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
                System.out.println(totalsims);
                if (!(totalsims.contains(event.getEntity().getPersistentID()))) {
                    //      System.out.println("adding sim");
                    name = event.getEntity();
                    unemployedsims.add(event.getEntity().getPersistentID());
                    totalsims.add(event.getEntity().getPersistentID());
                    unemployedsim.add((EntitySim) event.getEntity());
                    totalsim.add((EntitySim) event.getEntity());
                    System.out.println("added " + name + " " + event.getEntity().getPersistentID());
                    ((EntitySim) event.getEntity()).inlist = true;
                    sim = (event.getEntity().getPersistentID());
                    PacketHandler.INSTANCE.sendToAll(new Siminfo_packet(sim));
                }
            }
        }

    }}

