package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class AiSimShearSheep extends EntityAIBase {
    private EntitySim sim;
    private int shearcooldown = 60;
    private int workshift = 100;
    public AiSimShearSheep(EntitySim sim){
        this.sim = sim;

    }
    @Override
    public boolean shouldExecute() {
        return checkInvShears() && sim.getLabeledProfession().equals("Sheep Farmer") && sim.getSheeptarget() != null || sim.getHeldItemMainhand().getItem() instanceof ItemShears && !sim.getEndWork();
    }

    @Override
    public void startExecuting(){
        if (sim.getSheeptarget() != null){
        sim.getNavigator().tryMoveToEntityLiving(sim.getSheeptarget(),0.7d);
        if (!(sim.getHeldItemMainhand().getItem() instanceof ItemShears)){
            equipShears();
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting(){
        workshift--;
        if (workshift <= 0){
            sim.setEndWork(true);
            workshift = 100;
            return false;
        }
        return shouldExecute();
    }

    @Override
    public void updateTask(){

        if (shearcooldown <= 0 ){
            if (sim.getSheeptarget() != null){
            if (sim.getDistance(sim.getSheeptarget()) < 3){
                if (sim.getHeldItemMainhand().getItem() instanceof ItemShears){
                    shearSheep();
                    shearcooldown = 60;
                }
            }else{
                sim.getNavigator().tryMoveToEntityLiving(sim.getSheeptarget(),0.7d);

            }}
        }else{shearcooldown--;}
    }


    private void shearSheep(){
        IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH);

        sim.getLookHelper().setLookPosition(sim.posX,sim.posY,sim.posZ,360,360);
        List<ItemStack> stackList = sim.getSheeptarget().onSheared(sim.getHeldItemMainhand(),null,null,0);
        for (int i = 0;i<stackList.size();i++){
            ItemStack stack = stackList.get(i);
            ItemHandlerHelper.insertItemStacked(handler,stack,false);

            }
        sim.setSheeptarget(null);
        }

    private boolean checkInvShears(){
        IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        for (int i = 0;i<handler.getSlots();i++){
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.getItem() instanceof ItemShears){
                return true;
            }
        }
        return false;

    }

    private void equipShears(){
        IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH);
        for (int i = 0;i<handler.getSlots();i++){
            if (handler.getStackInSlot(i).getItem() instanceof ItemShears){
                sim.setHeldItem(EnumHand.MAIN_HAND,new ItemStack(Items.SHEARS,1));
                handler.getStackInSlot(i).shrink(1);
                break;
            }
        }
    }

}
