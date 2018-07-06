package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;

public class AISimEat extends EntityAIBase {
    private EntitySim sim;
    private int counter;
    public AISimEat(EntitySim sim)
    {
        this.sim = sim;
        setMutexBits(0);
    }
    @Override
    public boolean shouldExecute() {

        return sim != null && sim.getFoodLevel() < 15;
    }


    public boolean shouldContinueExecuting() {
        return false;
    }

    @Override
    public void startExecuting() {
        sim.Checkfood();

    }


}
