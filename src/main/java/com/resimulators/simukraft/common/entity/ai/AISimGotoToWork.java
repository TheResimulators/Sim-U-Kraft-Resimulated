package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.ai.pathfinding.CustomPathNavigateGround;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class AISimGotoToWork extends EntityAIBase {
    private  EntitySim sim;
    private int attempts = 0;
    public AISimGotoToWork(EntitySim sim){

        setMutexBits(8);
        this.sim = sim;
    }

    @Override
    public void startExecuting(){
        //System.out.println("is the executing")

        sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
        System.out.println("sims path " + sim.getNavigator().getPath());
        System.out.println(sim.getNavigator());
    }


    @Override
    public boolean shouldExecute() {
        if (sim.getJobBlockPos() != null){
        return !sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.getDistance(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY(),sim.getJobBlockPos().getX()) > 2.5d && !sim.getWorking();
    }
    return false;}

    @Override
    public boolean shouldContinueExecuting(){
        System.out.println("should continue exectuing " + shouldExecute());
        return shouldExecute();
    }
    @Override
    public void updateTask(){
        if (attempts < 5){
            attempts++;
            System.out.println("try to move to position " + sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d));

        } else{
            sim.attemptTeleport(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ());
            attempts = 0;
        }
    }
    @Override
    public void resetTask(){}
    }

