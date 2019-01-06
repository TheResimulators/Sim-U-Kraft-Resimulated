package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.ai.pathfinding.CustomPathNavigateGround;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public class AISimInteractGate extends EntityAIBase {

        protected EntitySim entity;
        protected BlockPos doorPosition = BlockPos.ORIGIN;
        /** The wooden door block */
        protected BlockFenceGate gate;
        /** If is true then the Entity has stopped Door Interaction and compoleted the task. */
        boolean hasStoppedDoorInteraction;
        float entityPositionX;
        float entityPositionZ;

    public AISimInteractGate(EntitySim entityIn)
        {
            setMutexBits(8);
            this.entity = entityIn;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (!this.entity.collidedHorizontally)
            {
                return false;
            }
            else
            {
                PathNavigateGround pathnavigateground = (PathNavigateGround)this.entity.getNavigator();
                Path path = pathnavigateground.getPath();
                if (path != null && !path.isFinished() && pathnavigateground.getEnterDoors())
                {
                    for (int i = 0; i < Math.min(path.getCurrentPathIndex()+2, path.getCurrentPathLength()); ++i)
                    {
                        PathPoint pathpoint = path.getPathPointFromIndex(i);
                        this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z);

                        if (this.entity.getDistanceSq((double)this.doorPosition.getX(), this.entity.posY, (double)this.doorPosition.getZ()) <= 5D)
                        {
                             this.gate = this.getBlockDoor(this.doorPosition);
                            if (this.gate != null)
                            {
                                return true;
                            }
                        }
                    }

                    this.doorPosition = (new BlockPos(this.entity));
                    this.gate = this.getBlockDoor(this.doorPosition);
                    return this.gate != null;
                }
                else
                {
                    return false;
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return !this.hasStoppedDoorInteraction;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.hasStoppedDoorInteraction = false;
            this.entityPositionX = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.entity.posX);
            this.entityPositionZ = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.entity.posZ);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            float f = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.entity.posX);
            float f1 = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.entity.posZ);
            float f2 = this.entityPositionX * f + this.entityPositionZ * f1;

            if (f2 < 0.0F)
            {
                this.hasStoppedDoorInteraction = true;
            }
        }

        private BlockFenceGate getBlockDoor(BlockPos pos)
        {
            IBlockState iblockstate = this.entity.world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            return block instanceof BlockFenceGate && iblockstate.getMaterial() == Material.WOOD ? (BlockFenceGate) block : null;
        }
    }

