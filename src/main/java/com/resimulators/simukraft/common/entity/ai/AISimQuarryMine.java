package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class AISimQuarryMine extends EntityAIBase {
    private World world;
    private EntitySim sim;
    private int miningdelay = 2;
    private BlockPos targetpos;
    private EnumFacing forward = null;
    private boolean pathfound = false;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int width = 0;
    private int depth = 0;
    private TileMiner miner;
    private int stairlevel = 0;
    private int xstairposition = 0;
    private int zstairposition = 0;
    //determines forwards or backwards;
    private int xstairdirection = 1;
    private int zstairdirection = 1;

    public AISimQuarryMine(EntitySim sim, World world ){
        this.sim = sim;
        this.world = world;
        setMutexBits(3);
    }
    @Override
    public boolean shouldExecute(){
        return sim.getProfession() == 7 && sim.isWorking();
    }
    @Override
    public void startExecuting(){
        x = 0;
        y = 0;
        z = 0;
        miner = (TileMiner)world.getTileEntity(sim.getJobBlockPos());
        width = miner.getWidth();
        depth = miner.getDepth();
    }


    @Override
    public void updateTask() {
        System.out.println(targetpos);
       /** if (targetpos != null && sim.getDistanceSq(targetpos) > 3 && !pathfound && sim.isWorking()) {
            Vec3d position = RandomPositionGenerator.findRandomTargetBlockTowards(sim, 3, 0, new Vec3d(targetpos.getX() + 0.5f, targetpos.getY(), targetpos.getZ() + 0.5f));
            System.out.println("position " + position);
            if (position != null) {
                pathfound = sim.getNavigator().tryMoveToXYZ(position.x, position.y, position.z, 0.7d);
                }
            }**/

        if (miningdelay <= 0){
            miningdelay = 5;
            if (targetpos != null){
                world.setBlockState(targetpos,Blocks.AIR.getDefaultState());
                targetpos = null;
            }}else miningdelay--;

            if (targetpos == null){
                targetpos = sim.getJobBlockPos().add(0,-1,0);
                targetpos = targetpos.offset(miner.getFacing());
                if (x > width){
                    x = 0;
                    z++;
                }
                if (z > depth){
                    x = 0;
                    z = 0;
                    y++;
                }

                targetpos = targetpos.offset(miner.getFacing().rotateY(),x);
                targetpos =targetpos.offset(miner.getFacing(),z);
                targetpos = targetpos.offset(EnumFacing.DOWN,y);


              /**  if (x == xstairposition && stairlevel==y){
                    placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().rotateY());
                    targetpos = null;
                    stairlevel++;
                    xstairposition+=xstairdirection;
                }
                if (x == xstairposition && x == width && stairlevel==y){
                    placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().rotateYCCW());
                    targetpos = null;
                    stairlevel++;
                    xstairdirection = -1;

                }
                if (z == zstairposition && stairlevel==y){
                    placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing());
                    targetpos = null;
                    stairlevel++;
                    zstairposition+=zstairdirection;
                }
                if (z == depth && z == zstairposition && stairlevel==y){
                    placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().getOpposite());
                    targetpos = null;
                    stairlevel++;
                    zstairdirection = -1;
                }**/
                x++;
            }
    }


    @Override
    public boolean shouldContinueExecuting(){
        return shouldExecute();
    }




    private void placeBlock(BlockPos pos,Block block,EnumFacing facing){
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
                    break;
                case 3:
                    world.setBlockState(pos,block.getDefaultState().withRotation(Rotation.COUNTERCLOCKWISE_90));
                    break;
            }}else {
                world.setBlockState(pos,block.getDefaultState());

            }
            System.out.println("this happened");

            sim.swingArm(EnumHand.MAIN_HAND);
            targetpos = null;


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
