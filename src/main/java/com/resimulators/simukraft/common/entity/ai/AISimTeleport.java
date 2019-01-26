package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class AISimTeleport extends EntityAIBase {
    private EntitySim sim;
    public AISimTeleport(EntitySim sim ){

        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return this.sim.isShouldteleport() && this.sim.getTeleporttarget() != null;

    }



    @Override
    public void startExecuting(){
        BlockPos pos = sim.getTeleporttarget();
        sim.world.spawnEntity(new EntityParticleSpawner(sim.world,sim));
        sim.setParticlspawning(true);
        sim.setTeleport(true);
    }
}
