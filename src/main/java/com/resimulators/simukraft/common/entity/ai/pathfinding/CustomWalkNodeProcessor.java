package com.resimulators.simukraft.common.entity.ai.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CustomWalkNodeProcessor extends WalkNodeProcessor {


    @Override
    protected PathNodeType getPathNodeTypeRaw(IBlockAccess p_189553_1_, int p_189553_2_, int p_189553_3_, int p_189553_4_){
        BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
        IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        if (block instanceof BlockFenceGate){
            return PathNodeType.DOOR_OPEN;
        }
        return super.getPathNodeTypeRaw(p_189553_1_,p_189553_2_,p_189553_3_,p_189553_4_);
    }
}
