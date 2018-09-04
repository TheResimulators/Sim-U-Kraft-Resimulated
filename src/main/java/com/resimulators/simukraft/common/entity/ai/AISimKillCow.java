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
        if (counter/20 == 5 && !killing && sim.getLabeledProfession().equals("Cattle Farmer") && sim.getDistanceSq(sim.getJobBlockPos()) > 2){
            killing = true;
            return super.shouldExecute();
        }else{
            counter++;
            return false;
        }
    }else {return false;}}


    @Override
    public void startExecuting(){
        List<Entity> animals = world.getEntitiesWithinAABBExcludingEntity(sim,new AxisAlignedBB(sim.posX-2,sim.posY,sim.posZ-2,sim.posX+2,sim.posY,sim.posZ+2));
        List<EntityCow> cows = new ArrayList<>();
        for (Entity entity: animals){
            if (entity instanceof EntityCow){
                cows.add((EntityCow) entity);
            }
            System.out.println("is this working");
            EntityCow cow = cows.get(rnd.nextInt(cows.size()-1));
            sim.setAttackTarget(cow);
            super.startExecuting();

        }
    }

    @Override
    public void resetTask(){
        killing = false;
    }

}
