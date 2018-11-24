package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
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

        sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
    }


    @Override
    public boolean shouldExecute() {
        if (sim.getJobBlockPos() != null && !sim.isParticlspawning() && !sim.getTeleport()) {
            if (sim.getDistance(sim.getJobBlockPos().getX(), sim.getJobBlockPos().getY(), sim.getJobBlockPos().getZ()) > 3) {
                EntityParticleSpawner spawner = new EntityParticleSpawner(sim.world, sim);
                spawner.setPosition(sim.getJobBlockPos().getX()+0.5f, sim.getJobBlockPos().getY(), sim.getJobBlockPos().getZ()+0.5f);
                sim.setParticlspawning(true);
                sim.world.spawnEntity(spawner);
                spawner.updateClient();
                sim.setTeleporttarget(sim.getJobBlockPos());
                sim.setTeleport(true);
                sim.setNoAI(true);
                return false;
            }


            return !sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.getDistance(sim.getJobBlockPos().getX(), sim.getJobBlockPos().getY(), sim.getJobBlockPos().getX()) > 2.5d && !sim.getWorking();
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
        }
    }
    @Override
    public void resetTask(){}
    }

