package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AISimKillCow extends EntityAIAttackMelee {
    private int counter = 0;
    private EntitySim sim;
    private World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
    private Random rnd = new Random();
    private boolean killing = false;
    public AISimKillCow(double speed, boolean memory,EntitySim sim){
        super(sim,speed,memory);
        this.sim = sim;
        setMutexBits(7);
    }
    @Override
    public boolean shouldExecute() {
        if (sim.getJobBlockPos() != null){
            System.out.println("sim distance " + sim.getDistanceSq(sim.getJobBlockPos()));
            System.out.println("counter/20 " + counter/20);
        if (counter/20 > 5 && !killing && sim.getLabeledProfession().equals("Cattle Farmer") && sim.getDistanceSq(sim.getJobBlockPos().add(0,1,0)) <=  2){
            killing = true;
            System.out.println("shoud execute " + super.shouldExecute());
            System.out.println("Sim Target " + sim.getAttackTarget());
            return super.shouldExecute();
        }else{
            counter++;
            return false;
        }
    }else {return false;}}


    @Override
    public void startExecuting(){
        System.out.println("started executing");
        BlockPos pos = sim.getJobBlockPos();
        List<Entity> animals = world.getEntitiesWithinAABBExcludingEntity(sim,new AxisAlignedBB(pos.getX()-3,pos.getY(),pos.getZ()-3,pos.getX()+3,pos.getY()+ 2,pos.getZ()+3));
        System.out.println("animals " + animals);
        List<EntityCow> cows = new ArrayList<>();
        for (Entity entity: animals){
            if (entity instanceof EntityCow){
                cows.add((EntityCow) entity);
            }
            System.out.println("axis alighnment " + new AxisAlignedBB(pos.getX()-3,pos.getY(),pos.getZ()-3,pos.getX()+3,pos.getY()+ 2,pos.getZ()+3));
            System.out.println("is this working");
            System.out.println("cow size " +cows.size());
            int randnum = rnd.nextInt(cows.size());
            System.out.println("random num " + randnum);
            EntityCow cow = cows.get(randnum);
            sim.setAttackTarget(cow);
            super.startExecuting();

        }
    }

    @Override
    public void resetTask(){
        killing = false;
    }

    @Override
    public boolean shouldContinueExecuting(){
        return super.shouldContinueExecuting();
        }
    @Override
    public void updateTask(){
        super.updateTask();
        if (!shouldContinueExecuting())cowKilled();
    }
    public void cowKilled(){
        killing = false;
        System.out.println("cow killed");

    }
}
