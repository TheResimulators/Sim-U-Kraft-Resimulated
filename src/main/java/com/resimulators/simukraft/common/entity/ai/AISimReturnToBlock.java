package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AISimReturnToBlock extends EntityAIBase {

    private EntitySim sim;
    private int tries = 5;
    public AISimReturnToBlock(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return checkInvSpace() && sim.isWorking() && !sim.isReturntoblock()&& !sim.isShouldteleport();
    }


    @Override
    public void startExecuting(){
        sim.setReturntoblock(true);



    }

    @Override
    public void resetTask(){
        sim.setReturntoblock(false);
    }



    @Override
    public void updateTask(){
        if (!sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY(),sim.getJobBlockPos().getZ(),sim.getAIMoveSpeed())) tries--;
        if (tries <= 0){
            sim.setShouldteleport(true);
            sim.setTeleporttarget(new BlockPos(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY(),sim.getJobBlockPos().getZ()));
            resetTask();
        }
    }
    private boolean checkInvSpace(){
        IItemHandler inv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);
        int minimium = 10;// used to check the minium amount of space that the sim has left to continue, each empty slot add 1, not full stack add 0.5. will need balancing
        int current = 0;
        for (int i = 0;i<inv.getSlots();i++){
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty())current+= 1;
            if (stack.getCount() < stack.getMaxStackSize()) current+=0.5;
        }
        if (current > minimium){
            return false;
        }
        return true;
    }
}
