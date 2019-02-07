package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class AISimQuarryMine extends EntityAIBase {
    private World world;
    private EntitySim sim;
    private int miningdelay = 10;
    private int hardness = 0;
    private BlockPos targetpos = null;
    private boolean placing = false;
    private EnumFacing forward = null;
    private boolean pathfound = false;
    private int progress = 1;
    AISimQuarryMine(EntitySim sim, World world ){
        this.sim = sim;
        this.world = world;
    }
    @Override
    public boolean shouldExecute(){
        return sim.getProfession() == 7 && sim.isWorking();
    }
    @Override
    public void startExecuting(){}


    @Override
    public void updateTask(){
        if (sim.getDistanceSq(targetpos) > 3 && !pathfound && targetpos != null){
            Vec3d position = RandomPositionGenerator.findRandomTargetBlockTowards(sim,3,0,new Vec3d(targetpos.getX()+0.5f,targetpos.getY(),targetpos.getZ()+0.5f));
            pathfound = sim.getNavigator().tryMoveToXYZ(position.x,position.y,position.z,0.7d);
        }
        if (miningdelay <= 0){
        if (hardness > 0 && sim.getDistanceSq(targetpos) < 4){
            sim.swingArm(EnumHand.MAIN_HAND);
            hardness-=1;
        }
        if (targetpos == null){
            forward = sim.getFacing();
            targetpos = sim.getJobBlockPos().add(0,-1,0);
            targetpos = targetpos.offset(forward);
            TileMiner miner = (TileMiner)world.getTileEntity(sim.getJobBlockPos());
            int depth = miner.getDepth();
            int width = miner.getWidth();
            targetpos = targetpos.offset(forward.rotateY(),progress%width);
            targetpos = targetpos.offset(forward,(progress/width)%depth);
            targetpos = targetpos.offset(EnumFacing.DOWN,(width*depth)%progress);
            if ((progress%width == 0 ||(progress/width)%depth == width) && (width*depth)%progress > (progress/width)%depth ){
            }else if ((progress%width == 0 ||(progress/width)%depth == width) && (width*depth)%progress == (progress/width)%depth ){
                if (progress%width == 0){
                    placeBlock(targetpos, Blocks.OAK_STAIRS,sim.getFacing());
                }else if ((progress/width%depth == width)) {
                    placeBlock(targetpos,Blocks.OAK_STAIRS,sim.getFacing().rotateY());
                }

            }
            Block block = world.getBlockState(targetpos).getBlock();
            if (!checkinvforitem(Item.getItemFromBlock(block),true)){
                targetpos = null;
            }else{
                hardness = (int)world.getBlockState(targetpos).getBlockHardness(world,targetpos);
            }
        }
        else if (hardness <= 0){

            Block block = world.getBlockState(targetpos).getBlock();
            world.setBlockState(targetpos, Blocks.AIR.getDefaultState());
            addItemtoInv(Item.getItemFromBlock(block));

        }


    }else {
            miningdelay--;
        }
    }


    @Override
    public boolean shouldContinueExecuting(){
        return shouldExecute();
    }




    private void placeBlock(BlockPos pos,Block block,EnumFacing facing){
        if (checkinvforitem(Item.getItemFromBlock(block),false)){
            if (block instanceof BlockStairs){
            EnumFacing blockfacing = block.getDefaultState().getValue(BlockStairs.FACING);
            int timestorotate = 0;
            while (blockfacing != facing){
                blockfacing.rotateY();
                timestorotate++;
            }
            switch (timestorotate){
                case 0:
                    world.setBlockState(pos,block.getDefaultState());
                    break;
                case 1:
                    world.setBlockState(pos,block.getDefaultState().withRotation(Rotation.CLOCKWISE_90));
                    break;
                case 2:
                    world.setBlockState(pos,block.getDefaultState().withRotation(Rotation.CLOCKWISE_180));
                case 3:
                    world.setBlockState(pos,block.getDefaultState().withRotation(Rotation.COUNTERCLOCKWISE_90));
            }}else {
                world.setBlockState(pos,block.getDefaultState());

            }

            sim.swingArm(EnumHand.MAIN_HAND);
            targetpos = null;


        }
    }


    private boolean checkinvforitem(@Nullable Item item,boolean adding){
        IItemHandler inv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

        for(int i = 0;i<inv.getSlots();i++){
            if (adding){
                if (inv.getStackInSlot(i).getItem().equals(item)){
                    if (inv.getStackInSlot(i).getMaxStackSize()> inv.getStackInSlot(i).getCount()){
                        return true;
                    }
                }
            }else{
                if (inv.getStackInSlot(i).equals(item)){
                    return true;
                }
            }
        }
        return false;
    }

    private void addItemtoInv(Item item){
        IItemHandler inv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

        for (int i = 0;i<inv.getSlots();i++){
            if (inv.getStackInSlot(i).getItem().equals(item) && inv.getStackInSlot(i).getCount() < inv.getStackInSlot(i).getMaxStackSize()){
                ItemStack stack = inv.getStackInSlot(i);
                stack.grow(1);
                inv.insertItem(i,stack,false);
            }
        }

    }
}
