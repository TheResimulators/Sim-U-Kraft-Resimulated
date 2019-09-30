package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class AISimTeleport extends EntityAIBase {
    private EntitySim sim;
    public AISimTeleport(EntitySim sim ){
        this.sim = sim;
        this.setMutexBits(0);
    }
    @Override
    public boolean shouldExecute() {
        return this.sim.isShouldteleport() && this.sim.getTeleporttarget() != null && !sim.isWorking();

    }



    @Override
    public void startExecuting(){
        BlockPos pos = sim.getTeleporttarget();
        EntityParticleSpawner spawner = new EntityParticleSpawner(sim.world,sim);
        spawner.setPosition(pos.getX(),pos.getY(),pos.getZ());
        sim.world.spawnEntity(spawner);
        spawner.updateClient();
        sim.setParticlspawning(true);
        sim.setTeleport(true);
        sim.setNoAI(true);
    }

    @Override
    public void updateTask(){
        if (shouldExecute()){
        if (sim.posX != sim.prevPosX ||sim.posY != sim.prevPosY || sim.posZ != sim.prevPosZ){
            sim.setPosition(sim.prevPosX,sim.prevPosY,sim.prevPosZ);
            }
            else{
            resetTask();
        }
        }
    }

    @Override
    public boolean shouldContinueExecuting(){
        if (!shouldExecute())resetTask();
        return shouldExecute();
    }

    @Override
    public void resetTask(){
        sim.setAIMoveSpeed(0.7f);
    }

    @Override
    public boolean isInterruptible(){
        return false;
    }

}
