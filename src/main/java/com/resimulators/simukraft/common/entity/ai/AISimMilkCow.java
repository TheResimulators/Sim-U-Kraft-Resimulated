package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.enums.FarmModes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AISimMilkCow extends EntityAIBase {
    private EntitySim sim;
    int milkdelay = 30;
    int worktimelimit;
    public AISimMilkCow(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return (checkInvBucket() && sim.getCowmode() == FarmModes.CowMode.MILK || sim.getHeldItemMainhand().getItem() instanceof ItemBucket) && !sim.getEndWork() && sim.getCowtarget() != null;
    }

    @Override
    public void startExecuting(){

        sim.getNavigator().tryMoveToXYZ(sim.getCowtarget().posX,sim.getCowtarget().posY,sim.getCowtarget().posZ,0.7d);
        if (!(sim.getHeldItemMainhand().getItem() instanceof ItemBucket)) equipBucket();

    }

    @Override
    public void updateTask(){

        if (milkdelay <= 0){

        if (sim.getDistance(sim.getCowtarget()) < 3) {
                if (sim.getHeldItemMainhand().getItem() instanceof ItemBucket) {
                    milkCow();


                }
            }else{ sim.getNavigator().tryMoveToXYZ(sim.getCowtarget().posX,sim.getCowtarget().posY,sim.getCowtarget().posZ,0.7d);}
            milkdelay = 30;
        }else{milkdelay--;}
    }

    private void milkCow(){
        if (milkdelay <= 0){
            ItemStack itemstack = sim.getHeldItemMainhand();
            if (itemstack.getItem() instanceof ItemBucket){
                sim.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                sim.getLookHelper().setLookPosition(sim.posX,sim.posY,sim.posZ,360,360);
                itemstack.shrink(1);
                sim.getCowtarget().getCapability(ModCapabilities.getCAP(),null).setmilked();
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
