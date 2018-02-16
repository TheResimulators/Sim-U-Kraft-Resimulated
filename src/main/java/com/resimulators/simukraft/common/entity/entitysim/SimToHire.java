package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;

public class SimToHire {
    public static ArrayList<String> Sims_name = new ArrayList<>();
    String name;

    @SubscribeEvent
    public void AvailableSims(LivingSpawnEvent event) {
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        }else{
            name = event.getEntity().getName();
            Sims_name.add(name);
            //System.out.println("added" + name);
        }
    System.out.println("added"+ name);
    }
}