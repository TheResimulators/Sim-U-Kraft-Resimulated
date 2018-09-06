package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;

public class AISimMilkCow extends EntityAIBase {

    private EntitySim sim;
    public AISimMilkCow(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        if(sim.getTarget() != null){
            return true;
        }
        return false;
    }


    @Override
    public void startExecuting(){
        if (sim.getDistance(sim.getTarget()) < 2){
            sim.swingArm(EnumHand.MAIN_HAND);
        }
    }
}
