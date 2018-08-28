package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class AISimGotoToWork extends EntityAIBase {
    private  EntitySim sim;
    private int tries = 5;
    private Random rnd = new Random();
    public AISimGotoToWork(EntitySim sim){
        this.sim = sim;
    }

    @Override
    public void startExecuting(){
        int xpos = rnd.nextInt(2)-1;
        int zpos = rnd.nextInt(2)-1;
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        System.out.println("start executing");
        boolean success = sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX()+xpos,world.getHeight(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getZ()),sim.getJobBlockPos().getZ()+zpos,0.7D);
        if (!success){
            if (tries == 0 ){
                sim.attemptTeleport(sim.getJobBlockPos().getX()+xpos,sim.getJobBlockPos().getY(),sim.getJobBlockPos().getZ()+zpos);
                tries = 5;
            }
            else{tries--;}
        }
    }
    @Override
    public boolean shouldExecute() {
        System.out.println("should eecute");

        return !sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.getJobBlockPos() != null;
    }
}
