package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class AISimGotoToWork extends EntityAIBase {
    private  EntitySim sim;
    private int attempts = 0;
    public AISimGotoToWork(EntitySim sim){

        setMutexBits(8);
        this.sim = sim;
    }

    @Override
    public void startExecuting(){
        sim.setStartingWork();
        sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
    }


    @Override
    public boolean shouldExecute() {
        if (sim.getJobBlockPos() != null && sim.isNotWorking()) {
            if (sim.getDistance(sim.getJobBlockPos().getX(), sim.getJobBlockPos().getY(), sim.getJobBlockPos().getZ()) > 4) {
                sim.setTeleporttarget(sim.getJobBlockPos());
                sim.setShouldteleport(true);
                return false;
            }


            return !sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.isStartingWork();
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting(){
        return shouldExecute();
    }
    @Override
    public void updateTask(){
        if (attempts < 5){
            sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
            attempts++;

        } else{
            sim.attemptTeleport(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ());
            attempts = 0;
            sim.setShouldteleport(true);
            sim.setTeleporttarget(sim.getJobBlockPos());

        }
    }
    @Override
    public void resetTask(){}
    }

