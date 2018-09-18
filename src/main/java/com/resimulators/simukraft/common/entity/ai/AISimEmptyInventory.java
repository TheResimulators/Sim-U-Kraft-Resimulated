package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class AISimEmptyInventory extends EntityAIBase {
    private EntitySim sim;

    public AISimEmptyInventory(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return sim.getEmptychest() != null && sim.getDistance(sim.getEmptychestpos().getX(), sim.getEmptychestpos().getY(), sim.getEmptychestpos().getZ()) < 4;
    }


    @Override
    public void startExecuting(){
        System.out.println("this has been executed");
        //gets slots and items of sim to trasfer. only transfers from drops inventory
        for (int i =0;i<sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getSlots();i++){
              ItemStack item = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i);
              for (int x = 0;x<sim.getEmptychest().getContainer(sim.world,sim.getEmptychestpos(),true).getSizeInventory();x++){
                  ItemStack cheststack = sim.getEmptychest().getContainer(sim.world,sim.getEmptychestpos(),true).getStackInSlot(x);
                  if (item.getItem().equals(cheststack.getItem()) && item.getCount() + cheststack.getCount() <= cheststack.getMaxStackSize()){
                      sim.getEmptychest().getContainer(sim.world,sim.getEmptychestpos(),true).setInventorySlotContents(x,new ItemStack(item.getItem(),item.getCount()+cheststack.getCount()));
                      sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).shrink(item.getCount());
                  }else if (cheststack.isEmpty()){
                      sim.getEmptychest().getContainer(sim.world,sim.getEmptychestpos(),true).setInventorySlotContents(x,new ItemStack(item.getItem(),item.getCount()));
                      sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).shrink(item.getCount());
                  }
              }
        }

    }
}
