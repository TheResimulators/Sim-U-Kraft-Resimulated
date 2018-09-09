package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;

public class AISimGetInventory extends EntityAIBase{
    private BlockChest chest;
    private EntitySim sim;
    public AISimGetInventory(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public boolean shouldExecute() {
        return sim.getEndWork();
    }


    @Override
    public void startExecuting(){
        for (int y = 0;y<2;y++) {
            for (int x = 0; x < 4; x++) {
                for (int z = 0; z < 4; z++) {
                    BlockPos checkpos = sim.getJobBlockPos().add(x-2,0,z-2);
                    Block block = sim.getEntityWorld().getBlockState(sim.getJobBlockPos().add(checkpos)).getBlock();
                    if (block instanceof BlockChest){
                        BlockChest chest = (BlockChest)block;
                        ILockableContainer container = chest.getContainer(sim.world,new BlockPos(checkpos),true);
                        int simsize = simGetnumberitems();
                        int chestavaliable = 0;
                        for (int i = 0;i<container.getSizeInventory();i++){
                            if (container.getStackInSlot(i).getItem().equals(Items.LEATHER) || container.getStackInSlot(i).getItem().equals(Items.BEEF) || container.getStackInSlot(i).isEmpty()){
                                chestavaliable++;
                            }
                        if (simsize<chestavaliable){
                                sim.setEmptychest(chest);
                                break;
                        }
                        }
                    }
                }
            }
        }
    }




    private int simGetnumberitems(){
        ArrayList<Item> items = new ArrayList();
        for(int i = 0; i<sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getSlots(); i++){
            if (!items.contains(sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem())){
                if (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem().equals(Items.LEATHER) ||sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem().equals(Items.BEEF));
                items.add(sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem());
            }
            return items.size();
        }
        return 0;
    }
}

