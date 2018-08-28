package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.Entity;
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

public class AISimKillCow extends EntityAIBase {
    private int counter = 0;
    private EntitySim sim;
    private World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
    private Random rnd = new Random();
    private boolean killing = false;
    public AISimKillCow(EntitySim sim ){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        if (counter/20 == 5 && !killing){
            killing = true;
            return true;
        }else{
            counter++;
            return false;
        }
    }


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
            sim.faceEntity(cow,0,0);
            cow.attackEntityFrom(DamageSource.causeMobDamage(sim),5);
            sim.swingArm(EnumHand.MAIN_HAND);

        }
    }

}
