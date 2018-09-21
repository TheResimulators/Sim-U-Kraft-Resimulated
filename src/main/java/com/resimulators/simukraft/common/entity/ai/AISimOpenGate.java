package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import static net.minecraft.block.BlockFenceGate.OPEN;

public class AISimOpenGate extends AISimInteractGate {


    /**
     * If the entity close the door
     */
    boolean closeDoor;
    /**
     * The temporisation before the entity close the door (in ticks, always 20 = 1 second)
     */
    int closeDoorTemporisation;

    public AISimOpenGate(EntitySim entitylivingIn, boolean shouldClose) {
        super(entitylivingIn);
        this.entity = entitylivingIn;
        this.closeDoor = shouldClose;
        setMutexBits(8);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.closeDoor && this.closeDoorTemporisation > 0 && super.shouldContinueExecuting();
    }
    /**

     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.closeDoorTemporisation = 40;
        this.OnSimOpen(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld(),doorPosition,true);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        if (this.closeDoor) {
            OnSimOpen(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld(),doorPosition,false);
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
        --this.closeDoorTemporisation;
        super.updateTask();
        if (!shouldContinueExecuting()) resetTask();
    }


    public boolean OnSimOpen(World worldIn, BlockPos pos,boolean open){
        System.out.println("facing from the world " + worldIn.getBlockState(pos).getValue(BlockHorizontal.FACING));
        IBlockState state = worldIn.getBlockState(pos);
        state = state.withProperty(OPEN,open);
        worldIn.setBlockState(pos,state,10);
        return true;
    }

    public boolean shouldExecute(){
        System.out.println("should execute " + super.shouldExecute());
        return super.shouldExecute();
    }
}