package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.enums.cattleFarmMode;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
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
        return (checkInvBucket() && sim.getCowmode() == cattleFarmMode.FarmMode.MILK || sim.getHeldItemMainhand().getItem() instanceof ItemBucket) && !sim.getEndWork() && sim.getTarget() != null;
    }

    @Override
    public void startExecuting(){

        sim.getNavigator().tryMoveToXYZ(sim.getTarget().posX,sim.getTarget().posY,sim.getTarget().posZ,0.7d);
        if (!(sim.getHeldItemMainhand().getItem() instanceof ItemBucket)) equipBucket();

    }

    @Override
    public void updateTask(){

        if (milkdelay <= 0){

        if (sim.getDistance(sim.getTarget()) < 3) {
                if (sim.getHeldItemMainhand().getItem() instanceof ItemBucket) {
                    milkCow();


                }
            }else{ sim.getNavigator().tryMoveToXYZ(sim.getTarget().posX,sim.getTarget().posY,sim.getTarget().posZ,0.7d);}
            milkdelay = 30;
        }else{milkdelay--;}}
    private void milkCow(){
        if (milkdelay <= 0){
            ItemStack itemstack = sim.getHeldItemMainhand();
            System.out.println("this is being called");
            if (itemstack.getItem() instanceof ItemBucket){
                sim.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                sim.getTarget().playSound(SoundEvents.ENTITY_COW_AMBIENT,0.5f,1.0f);
                sim.getLookHelper().setLookPosition(sim.posX,sim.posY,sim.posZ,360,360);
                itemstack.shrink(1);
                sim.getTarget().getCapability(ModCapabilities.getCAP(),null).setmilked();
                if (!addMilkBucket()) {
                    sim.dropItem(new ItemStack(Items.MILK_BUCKET).getItem(), 1);
                }
                sim.setTargetCow(null);
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
        return sim.getHeldItemMainhand().getItem() instanceof ItemBucket;
        }
    }
