package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class SimToHire {
    public static List<EntitySim> sims = new ArrayList<>();
    Entity name;

    @SubscribeEvent
    @SideOnly(CLIENT)
    public void availableSims(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            if (!((EntitySim) event.getEntity()).inlist) {
                System.out.println("adding sim");
                name = event.getEntity();
                sims.add((EntitySim) name);
                System.out.println("added" + name);
                ((EntitySim) event.getEntity()).inlist = true;
                return;
            }
        }
    }
}