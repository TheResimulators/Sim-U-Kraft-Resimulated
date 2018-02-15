package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class SimToHire {
    ArrayList<String> sims = new ArrayList<String>;

    public void Avalablesims(LivingSpawnEvent event){
        if (!(event.getEntity() instanceof EntitySim)){
            return;

        }

    }
}
