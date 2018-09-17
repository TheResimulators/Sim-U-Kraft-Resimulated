package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.enums.cattleFarmMode;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AISimGetBuckets extends EntityAIBase {
    private EntitySim sim;
    int milkdelay = 30;
    int worktimelimit;
    public AISimGetBuckets(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return (checkInvBucket() && sim.getCowmode() == cattleFarmMode.FarmMode.MILK || sim.getHeldItemMainhand().getItem() instanceof ItemBucket) && !sim.getEndWork();
    }

    @Override
    public void startExecuting(){

        sim.getNavigator().tryMoveToXYZ(sim.getTarget().posX,sim.getTarget().posY,sim.getTarget().posZ,0.7d);
        equipBucket();

    }

    @Override
    public void updateTask(){

        if (milkdelay < 0){
            milkdelay = 30;
        if (sim.getDistance(sim.getTarget()) < 1) {
                if (sim.getHeldItemMainhand().getItem() instanceof ItemBucket) {
                    sim.swingArm(EnumHand.MAIN_HAND);
                    sim.getJumpHelper().doJump();
                    milkCow();

                }
            }else{ sim.getNavigator().tryMoveToXYZ(sim.getTarget().posX,sim.getTarget().posY,sim.getTarget().posZ,0.7d);}

        }else{milkdelay--;}}
    private void milkCow(){
        ItemStack itemstack = sim.getHeldItemMainhand();
        System.out.println("this is being called");
        if (itemstack.getItem() instanceof ItemBucket);
        {
            sim.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            sim.getTarget().playSound(SoundEvents.ENTITY_COW_AMBIENT,0.5f,1.0f);
            sim.swingArm(EnumHand.MAIN_HAND);
            itemstack.shrink(1);
            if (checkInvBucket()){
                if (itemstack.isEmpty()) equipBucket();
            }
            if (!addMilkBucket()) {
                sim.dropItem(new ItemStack(Items.MILK_BUCKET).getItem(), 1);
            }

        }
}



    private boolean addMilkBucket(){
        IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH);
        for (int i = 0;i<handler.getSlots();i++){
             if (handler.getStackInSlot(i).isEmpty()){
                 handler.insertItem(i,new ItemStack(Items.MILK_BUCKET,1),false);
                 return true;
             }
        }
        return false;
        }



        private void equipBucket(){
            IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH);
            for (int i = 0;i<handler.getSlots();i++){
                if ((handler.getStackInSlot(i).getItem() instanceof ItemBucket)){
                    sim.setHeldItem(EnumHand.MAIN_HAND,new ItemStack(Items.BUCKET,1));
                    handler.getStackInSlot(i).shrink(1);
                    return;
                }
            }
        }

        private boolean checkInvBucket(){
            IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH);
            for (int i = 0;i<handler.getSlots();i++){
                if (handler.getStackInSlot(i).getItem() instanceof ItemBucket){
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            worktimelimit--;

            if (worktimelimit <= 0){
                sim.setEndWork(true);
                worktimelimit = 100;
                return false;

            }
        return checkInvBucket();
        }
    }
