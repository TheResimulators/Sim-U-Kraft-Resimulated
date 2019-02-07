package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class AISimKillCow extends EntityAIBase{
    private EntitySim sim;
    private World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
    private Random rnd = new Random();
    private boolean killing = false;
    public AISimKillCow(EntitySim sim){
        this.sim = sim;
        setMutexBits(7);
    }
    @Override
    public boolean shouldExecute() {
        if (sim.getJobBlockPos() != null && sim.getLabeledProfession().equals("Cattle Farmer") || sim.getLabeledProfession().equals("Sheep Farmer")) {
//            System.out.println("Should execute");
  //          System.out.println("Blockpos " + sim.getJobBlockPos() + " sim pos " + sim.getPosition());
    //        System.out.println("Distance " + sim.getDistance(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+ 1,sim.getJobBlockPos().getZ()));
      //      System.out.println("executing " + (sim.getLabeledProfession().equals("Cattle Farmer") && sim.getDistanceSq(sim.getJobBlockPos().add(0, 1, 0)) <= 3));
        //    System.out.println("job " + sim.getLabeledProfession().equals("Cattle Farmer"));
            return sim.getDistance(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY(),sim.getJobBlockPos().getZ()) <= 3 && !sim.isWorking() && !sim.getLabeledProfession().equals("Nitwit");
        }else {return false;
        }
    }


    @Override
    public void startExecuting(){
        sim.setWorking();
        System.out.println("Sim working " + sim.isWorking());
        }

    @Override
    public void resetTask(){
        System.out.println(" is this being reached");
    }

    @Override
    public boolean shouldContinueExecuting(){
        return false;}
    @Override
    public void updateTask(){
    }

    @Override
    public boolean isInterruptible(){
        return true;
    }
}
