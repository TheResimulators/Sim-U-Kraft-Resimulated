package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;

public class SimToHire {
    public static List<EntitySim> Sims = new ArrayList<>();
    Entity name;

    @SubscribeEvent
    public void AvailableSims(LivingSpawnEvent event) {
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        }else{
            name = event.getEntity();
            Sims.add((EntitySim) name);
            System.out.println("added" + name);
        }
    }
}