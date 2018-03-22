package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class SimToHire {

    public static List<EntitySim> unemployedsims = new ArrayList<>();
    public static List<EntitySim> totalsims = new ArrayList<>();

    Entity name;

    @SubscribeEvent
    @SideOnly(CLIENT)
    public void availableSims(EntityJoinWorldEvent event) {
        World world = Minecraft.getMinecraft().world;
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            if (!world.isRemote) {
                if (!((EntitySim) event.getEntity()).inlist) {
                    System.out.println("adding sim");
                    name = event.getEntity();
                    unemployedsims.add((EntitySim) name);
                    totalsims.add((EntitySim) name);
                    System.out.println("added" + name);
                    ((EntitySim) event.getEntity()).inlist = true;
                    return;
                }
            }

            NBTTagList nbttaglist = new NBTTagList();
             for (int i = 0;i < totalsims.size(); i++){
                 UUID sim_id = totalsims.get(i).getUniqueID();
                 NBTTagCompound sim = new NBTTagCompound();
                 sim.setUniqueId("sim" + i,sim_id);
             }
            
        }
    }

}

