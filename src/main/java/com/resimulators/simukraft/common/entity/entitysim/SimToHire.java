package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.List;

public class SimToHire {
    public static List<EntitySim> sims = new ArrayList<>();
    Entity name;

    @SubscribeEvent
    public void availableSims(EntityEvent event) {
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            if (!sims.contains(event.getEntity())) {
                name = event.getEntity();
                sims.add((EntitySim) name);
                System.out.println("added" + name);
            }
        }
    }
}