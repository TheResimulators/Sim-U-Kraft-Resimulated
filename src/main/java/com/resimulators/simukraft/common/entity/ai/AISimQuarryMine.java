package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AISimQuarryMine extends EntityAIBase {
    private World world;
    private EntitySim sim;
    private int miningdelay = 2;
    private BlockPos targetpos;
    private EnumFacing forward = null;
    private boolean pathfound = false;
    private int stairposy = 0;
    private int stairposx = 0;
    private int stairposz = 0;
    // direction, 0 = right, 1 = forward, 2 = left, 3 = backwards
    private int direction = 0;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int width = 0;
    private int depth = 0;
    private TileMiner miner;
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
        direction = 0;
        stairposz = 0;
        stairposx = 0;
        stairposy = 0;
        miner = (TileMiner)world.getTileEntity(sim.getJobBlockPos());
        width = miner.getWidth();
        depth = miner.getDepth();
    }


    @Override
    public void updateTask() {
        /** if (targetpos != null && sim.getDistanceSq(targetpos) > 3 && !pathfound && sim.isWorking()) {
            Vec3d position = RandomPositionGenerator.findRandomTargetBlockTowards(sim, 3, 0, new Vec3d(targetpos.getX() + 0.5f, targetpos.getY(), targetpos.getZ() + 0.5f));
            System.out.println("position " + position);
            if (position != null) {
                pathfound = sim.getNavigator().tryMoveToXYZ(position.x, position.y, position.z, 0.7d);
                }
            }**/

        if (miningdelay <= 0){
            miningdelay = 1;
            if (targetpos != null){
                world.setBlockState(targetpos,Blocks.AIR.getDefaultState());
                targetpos = null;
            }}else miningdelay--;

            if (targetpos == null){
                targetpos = sim.getJobBlockPos().add(0,-1,0);
                targetpos = targetpos.offset(miner.getFacing());
                targetpos = targetpos.offset(miner.getFacing().rotateY());
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
                if (targetpos.getY() < 1 || world.getBlockState(targetpos).getBlock().equals(Blocks.BEDROCK)){

                    targetpos = null;
                    sim.setNotWorking();
                    SaveSimData.get(world).getFaction(sim.getFactionId()).addUnemployedSim(sim.getUniqueID());
                    ((TileMiner) world.getTileEntity(sim.getJobBlockPos())).setHired(false);
                    ((TileMiner) world.getTileEntity(sim.getJobBlockPos())).setId(null);
                    List<UUID> players = SaveSimData.get(world).getFaction(sim.getFactionId()).getPlayers();
                    for (UUID id:players){
                        EntityPlayer player = world.getPlayerEntityByUUID(id);
                        player.sendMessage(new TextComponentString("Miner " +sim.getName() + " has finished mining at position " + sim.getJobBlockPos() +"\n and has been fired" ));
                    }
                    sim.setJobBlockPos(null);
                }else{
                    if (y == stairposy){
                        //to the right of the miner block

                        if (targetpos == miner.getPos().offset(miner.getFacing(), depth).offset(miner.getFacing().rotateY(), width - 1).offset(EnumFacing.DOWN, y)){
                            System.out.println("this should go when it is at the stupid spot of not placing!!!!");
                        }

                        if (z == 0 && direction == 0 && x == stairposx){
                            if (x == width){
                                direction = 1;
                                stairposz += 1;
                                placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().getOpposite());
                            }else placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().rotateYCCW());
                            stairposx+= 1;
                            stairposy+= 1;
                        }
                        //direction same facing as the miner blocks
                        if (x == width && direction == 1 && z == stairposz){
                            stairposz += 1;
                            stairposy+= 1;
                            if (z == depth){
                                direction = 2;
                                if (stairposx > 0) stairposx -= 1;
                            }else placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().getOpposite());
                        }
                        //from right to left
                        if (z == depth && direction == 2 && x == stairposx){
                            if (x == 0){
                                direction = 3;
                                stairposz -= 1;
                                placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing());
                            }else{
                                placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing().rotateY());
                            }
                            stairposx -= 1;
                            stairposy+= 1;

                        }
                        //comming back towards the miner block
                        if (x == 0 && direction == 3 && stairposz == z){
                            placeBlock(targetpos,Blocks.OAK_STAIRS,miner.getFacing());
                            stairposz -= 1;
                            stairposy+= 1;
                            if (z == 0){
                                direction = 0;
                                stairposx += 1;
                            }
                        }

                    }
                x++;}}}

    @Override
    public boolean shouldContinueExecuting(){
        return shouldExecute();
    }




    private void placeBlock(BlockPos pos,Block block,EnumFacing facing){
            if (block instanceof BlockStairs && pos != null){
            EnumFacing blockfacing = block.getDefaultState().getValue(BlockStairs.FACING);
            int timestorotate = 0;
            while (blockfacing != facing){
                blockfacing = blockfacing.rotateY();
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
                    world.setBlockState(pos,block.getDefaultState().withRotation(Rotation.CLOCKWISE_180).withRotation(Rotation.CLOCKWISE_90));
                    break;
                default:
                    world.setBlockState(pos,block.getDefaultState());
                    break;
            }}
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
