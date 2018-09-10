package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AISimEmptyInventory extends EntityAIBase {
    private EntitySim sim;

    public AISimEmptyInventory(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return sim.getEmptychest() != null && sim.getDistance(sim.getEmptychestpos().getX(), sim.getEmptychestpos().getY(), sim.getEmptychestpos().getZ()) < 4;
    }


    @Override
    public void startExecuting(){
        for (int i =0;i<sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getSlots();i++){
              
        }

    }
}
